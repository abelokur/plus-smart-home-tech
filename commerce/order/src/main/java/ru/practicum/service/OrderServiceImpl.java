package ru.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.client.DeliveryClient;
import ru.practicum.client.PaymentClient;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.OrderState;
import ru.practicum.dto.order.ProductReturnRequest;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.exception.NoOrderFoundException;
import ru.practicum.exception.NotAuthorizedUserException;
import ru.practicum.mapper.AddressMapper;
import ru.practicum.mapper.OrderMapper;
import ru.practicum.model.Order;
import ru.practicum.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сервис для управления заказами.
 * Координирует создание и обработку заказов, взаимодействуя с другими сервисами:
 * складом, доставкой и платежами.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final WarehouseClient warehouseClient;
    private final TransactionTemplate transactionTemplate;

    /**
     * Получает список заказов пользователя с пагинацией.
     *
     * @param username имя пользователя
     * @param pageable параметры пагинации
     * @return страница с заказами пользователя
     * @throws NotAuthorizedUserException если username равен null
     */
    @Override
    public Page<OrderDto> getOrders(String username, Pageable pageable) {
        if (username == null) {
            throw new NotAuthorizedUserException("User =  " + username + " is not authorized");
        }

        return orderMapper.toDto(orderRepository.findByUsername(username, pageable));
    }

    /**
     * Создает новый заказ.
     * Выполняет последовательность шагов:
     * <ol>
     *   <li>Создание заказа и бронирование товаров на складе</li>
     *   <li>Планирование доставки</li>
     *   <li>Расчет стоимости доставки и товаров</li>
     *   <li>Создание платежа</li>
     * </ol>
     * В случае ошибки выполняется откат всех выполненных действий.
     *
     * @param request данные для создания заказа
     * @return созданный заказ
     */
    @Override
    @Transactional
    public OrderDto createOrder(CreateNewOrderRequest request) {
        // 1. Создание заказа и бронирование товаров на складе
        Order order = createOrderEntity(request);

        try {
            // 2. Планирование доставки
            planDelivery(order, request.deliveryAddress());

            // 3. Расчет стоимости
            calculateAndSetPrices(order);

            // 4. Создание платежа
            createPayment(order);
        } catch (Exception e) {
            //отменяем бронирование товаров и доставку,
            //т.к. платеж не создастся без всех предыдущих этапов, то откат платежа не нужен
            if (order.getDeliveryId() != null) {
                deliveryClient.cancelDelivery(order.getDeliveryId());
            }
            if (order.getOrderId() != null) {
                warehouseClient.cancelAssemblyProductForOrder(order.getOrderId());
            }

            //пробрасываем исключение дальше,
            //что бы не создавался заказ и была видна причина почему это произошло.
            throw e;
        }

        return orderMapper.toDto(order);
    }

    /**
     * Обрабатывает возврат товаров из заказа.
     * Возвращает товары на склад и обновляет статус заказа.
     *
     * @param request данные для возврата товаров
     * @return обновленный заказ
     */
    @Override
    @Transactional
    public OrderDto returnOrder(ProductReturnRequest request) {
        Order order = getOrder(request.orderId());

        if (order.getState() != OrderState.PRODUCT_RETURNED) {
            warehouseClient.returnToWarehouse(request.products());
        }

        order.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.toDto(order);
    }

    /**
     * Обрабатывает успешную оплату заказа.
     * Вызывается из сервиса платежей.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @Override
    public OrderDto paymentSuccess(UUID orderId) {
        return setOrderState(orderId, OrderState.PAID);
    }

    /**
     * Обрабатывает неудачную оплату заказа.
     * Вызывается из сервиса платежей.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @Override
    public OrderDto paymentFailed(UUID orderId) {
        return setOrderState(orderId, OrderState.PAYMENT_FAILED);
    }

    /**
     * Обрабатывает успешную доставку заказа.
     * Вызывается из сервиса доставки.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        return setOrderState(orderId, OrderState.DELIVERED);
    }

    /**
     * Обрабатывает неудачную доставку заказа.
     * Вызывается из сервиса доставки.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        return setOrderState(orderId, OrderState.DELIVERY_FAILED);
    }

    /**
     * Отмечает заказ как завершенный.
     * Вызов сторонним сервисом в рамках ТЗ не предусмотрен.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @Override
    public OrderDto completedOrder(UUID orderId) {
        return setOrderState(orderId, OrderState.COMPLETED);
    }

    /**
     * Рассчитывает общую стоимость заказа.
     * Запрашивает расчет у сервиса платежей.
     *
     * @param orderId идентификатор заказа
     * @return заказ с рассчитанной стоимостью
     */
    @Override
    @Transactional
    public OrderDto calculateTotal(UUID orderId) {
        Order order = getOrder(orderId);
        order.setTotalPrice(paymentClient.getTotalCost(orderMapper.toDto(order)));
        return orderMapper.toDto(order);
    }

    /**
     * Рассчитывает стоимость доставки для заказа.
     * Запрашивает расчет у сервиса доставки.
     *
     * @param orderId идентификатор заказа
     * @return заказ с рассчитанной стоимостью доставки
     */
    @Override
    @Transactional
    public OrderDto calculateDelivery(UUID orderId) {
        Order order = getOrder(orderId);
        order.setDeliveryPrice(deliveryClient.deliveryCost(orderMapper.toDto(order)));
        return orderMapper.toDto(order);
    }

    /**
     * Отмечает заказ как собранный.
     * Вызывается из сервиса доставки.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @Override
    @Transactional
    public OrderDto assemblyOrder(UUID orderId) {
        return setOrderState(orderId, OrderState.ASSEMBLED);
    }

    /**
     * Обрабатывает неудачную сборку заказа.
     * Вызов сторонним сервисом в рамках ТЗ не предусмотрен.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        return setOrderState(orderId, OrderState.ASSEMBLY_FAILED);
    }

    /**
     * Устанавливает новый статус для заказа.
     * Выполняется в транзакции.
     *
     * @param orderId идентификатор заказа
     * @param state   новый статус
     * @return обновленный заказ
     * @throws IllegalArgumentException если orderId или state равны null
     * @throws NoOrderFoundException    если заказ не найден
     */
    private OrderDto setOrderState(UUID orderId, OrderState state) {
        if (orderId == null || state == null) {
            throw new IllegalArgumentException("Order id and state cannot be null");
        }
        return transactionTemplate.execute(status -> {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new NoOrderFoundException("No order found for id = " + orderId));
            if (order.getState() == state) {
                log.info("Order {} is already in state {}", orderId, state);
            } else {
                log.info("Order {} is in state {}, changing to {}", orderId, order.getState(), state);
                order.setState(state);
            }
            return orderMapper.toDto(order);
        });
    }

    /**
     * Находит заказ по идентификатору.
     *
     * @param orderId идентификатор заказа
     * @return найденный заказ
     * @throws NoOrderFoundException если заказ не найден
     */
    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("No order found for id = " + orderId));
    }

    /**
     * Создает сущность заказа и резервирует товары на складе.
     *
     * @param request данные для создания заказа
     * @return созданный заказ с характеристиками доставки
     */
    private Order createOrderEntity(CreateNewOrderRequest request) {
        ShoppingCartDto shoppingCart = request.shoppingCart();

        // 1. Создаем базовый заказ и делаем промежуточное сохранение для получения ID
        Order order = Order.builder()
                .shoppingCartId(shoppingCart.shoppingCartId())
                .products(shoppingCart.products())
                .state(OrderState.NEW)
                .username(shoppingCart.username())
                .address(addressMapper.toEntity(request.deliveryAddress()))
                .build();

        order = orderRepository.save(order);

        // 2. Создаем запрос на сборку товаров
        AssemblyProductsForOrderRequest assemblyRequest =
                new AssemblyProductsForOrderRequest(order.getProducts(), order.getOrderId());

        BookedProductsDto bookedProducts = warehouseClient.assemblyProductForOrderFromShoppingCart(assemblyRequest);

        order.setDeliveryWeight(bookedProducts.deliveryWeight());
        order.setDeliveryVolume(bookedProducts.deliveryVolume());
        order.setFragile(bookedProducts.fragile());

        return orderRepository.save(order);
    }

    /**
     * Планирует доставку для заказа.
     *
     * @param order           заказ
     * @param deliveryAddress адрес доставки
     */
    private void planDelivery(Order order, AddressDto deliveryAddress) {
        AddressDto fromAddress = warehouseClient.getWarehouseAddress();

        DeliveryDto deliveryDto = DeliveryDto.builder()
                .fromAddress(fromAddress)
                .toAddress(deliveryAddress)
                .orderId(order.getOrderId())
                .build();

        deliveryDto = deliveryClient.planDelivery(deliveryDto);
        order.setDeliveryId(deliveryDto.deliveryId());
        orderRepository.save(order);
    }

    /**
     * Рассчитывает и устанавливает цены для заказа.
     *
     * @param order заказ
     */
    private void calculateAndSetPrices(Order order) {
        OrderDto orderDto = orderMapper.toDto(order);

        //Получаем стоимости доставки и товаров
        BigDecimal deliveryPrice = deliveryClient.deliveryCost(orderDto);
        BigDecimal productPrice = paymentClient.productCost(orderDto);

        //Устанавливаем стоимость доставки и товаров
        //для дальнейшего расчета общей стоимости
        order.setDeliveryPrice(deliveryPrice);
        order.setProductPrice(productPrice);

        //Получаем общую стоимость заказа
        BigDecimal totalPrice = paymentClient.getTotalCost(orderMapper.toDto(order));
        order.setTotalPrice(totalPrice);

        //Обновляем данные в репозитории
        orderRepository.save(order);
    }

    /**
     * Создает платеж для заказа.
     *
     * @param order заказ
     */
    private void createPayment(Order order) {
        PaymentDto paymentDto = paymentClient.payment(orderMapper.toDto(order));
        order.setPaymentId(paymentDto.paymentId());
        orderRepository.save(order);
        log.debug("Payment {} created for order {}", paymentDto.paymentId(), order.getOrderId());
    }
}