package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.client.OrderClient;
import ru.practicum.client.ShoppingStoreClient;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.dto.payment.PaymentStatus;
import ru.practicum.exception.NoPaymentFoundException;
import ru.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.practicum.mapper.PaymentMapper;
import ru.practicum.model.Payment;
import ru.practicum.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

/**
 * Сервис для управления платежами.
 * Обрабатывает создание платежей, расчеты стоимости и обработку статусов.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;
    private final TransactionTemplate transactionTemplate;
    private final BigDecimal TAX_PERCENTAGE = BigDecimal.valueOf(0.1);

    /**
     * Создает платеж для заказа.
     * Проверяет данные заказа и создает платеж со статусом PENDING.
     *
     * @param orderDto данные заказа
     * @return созданный платеж
     * @throws NotEnoughInfoInOrderToCalculateException если недостаточно данных для создания платежа
     */
    @Override
    public PaymentDto payment(OrderDto orderDto) {
        checkOrderDto(orderDto);

        Payment payment = Payment.builder()
                .orderId(orderDto.orderId())
                .totalPayment(orderDto.totalPrice())
                .productTotal(orderDto.productPrice())
                .deliveryTotal(orderDto.deliveryPrice())
                .taxTotal(calculateTax(orderDto.productPrice()))
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    /**
     * Рассчитывает общую стоимость заказа.
     * Включает стоимость товаров, доставки и налоги.
     *
     * @param orderDto данные заказа
     * @return общая стоимость заказа, округленная до 2 знаков
     * @throws NotEnoughInfoInOrderToCalculateException если недостаточно данных для расчета
     */
    @Override
    public BigDecimal getTotalCost(OrderDto orderDto) {
        BigDecimal deliveryTotal = orderDto.deliveryPrice();
        BigDecimal productTotal = orderDto.productPrice();

        if (deliveryTotal == null || productTotal == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough info in order to calculate cost");
        }

        return deliveryTotal
                .add(productTotal)
                .add(calculateTax(productTotal))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Обрабатывает успешный платеж (refund согласно OpenAPI).
     * Обновляет статус платежа на SUCCESS и уведомляет сервис заказов.
     *
     * @param paymentId идентификатор платежа
     */
    @Override
    public void refund(UUID paymentId) {
        log.info("Processing payment SUCCESS for paymentId: {}", paymentId);

        Payment payment = transactionTemplate.execute(status -> {
            Payment paymentInProgress = getPayment(paymentId);

            if (paymentInProgress.getPaymentStatus() == PaymentStatus.SUCCESS) {
                log.info("Payment {} already in SUCCESS state", paymentId);
                return paymentInProgress;
            }

            log.debug("Updating payment {} status from {} to SUCCESS",
                    paymentId, paymentInProgress.getPaymentStatus());

            paymentInProgress.setPaymentStatus(PaymentStatus.SUCCESS);
            return paymentInProgress;
        });

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            try {
                log.info("Notifying order service about successful payment for order: {}",
                        payment.getOrderId());
                orderClient.paymentSuccess(payment.getOrderId());
                log.info("Successfully notified order service");
            } catch (Exception e) {
                log.warn("Failed to notify order service about SUCCESSFUL payment: {}",
                        paymentId, e);
            }
        }
    }

    /**
     * Рассчитывает стоимость товаров в заказе.
     * Запрашивает цены товаров у сервиса магазина и умножает на количество.
     *
     * @param orderDto данные заказа
     * @return стоимость товаров, округленная до 2 знаков
     * @throws NotEnoughInfoInOrderToCalculateException если в заказе нет товаров
     */
    @Override
    public BigDecimal productCost(OrderDto orderDto) {
        Map<UUID, Long> products = orderDto.products();

        if (products == null || products.isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("No products in order to calculate cost");
        }

        return products.entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    BigDecimal quantity = BigDecimal.valueOf(entry.getValue());
                    BigDecimal price = shoppingStoreClient.getProduct(productId).price();
                    return price.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Обрабатывает неудачный платеж.
     * Обновляет статус платежа на FAILED и уведомляет сервис заказов.
     *
     * @param paymentId идентификатор платежа
     * @throws IllegalStateException если попытка перевести платеж из SUCCESS в FAILED
     */
    @Override
    public void failed(UUID paymentId) {
        Payment payment = transactionTemplate.execute(status -> {
            Payment paymentInProgress = getPayment(paymentId);

            // Если уже в FAILED - ничего не делаем
            if (paymentInProgress.getPaymentStatus() == PaymentStatus.FAILED) {
                log.info("Payment {} already in FAILED state", paymentId);
                return paymentInProgress;
            }

            // Нельзя перейти из SUCCESS в FAILED
            if (paymentInProgress.getPaymentStatus() == PaymentStatus.SUCCESS) {
                log.warn("Attempt to move payment {} from SUCCESS to FAILED", paymentId);
                throw new IllegalStateException("Cannot move payment from SUCCESS to FAILED");
            }

            paymentInProgress.setPaymentStatus(PaymentStatus.FAILED);
            return paymentInProgress;
        });

        if (payment.getOrderId() != null) {
            try {
                orderClient.paymentFailed(payment.getOrderId());
            } catch (Exception e) {
                log.warn("Failed to notify order service about failed payment: {}", paymentId, e);
            }
        }
    }

    /**
     * Находит платеж по идентификатору.
     *
     * @param paymentId идентификатор платежа
     * @return найденный платеж
     * @throws NoPaymentFoundException если платеж не найден
     */
    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findPaymentByPaymentId(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException("Payment with id = " + paymentId + " not found"));
    }

    /**
     * Проверяет данные заказа на полноту.
     *
     * @param orderDto данные заказа
     * @throws NotEnoughInfoInOrderToCalculateException если данные неполные
     */
    private void checkOrderDto(OrderDto orderDto) {
        if (orderDto == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Order is null");
        }
        if (orderDto.deliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Delivery price is null");
        }
        if (orderDto.productPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Product price is null");
        }
        if (orderDto.totalPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Total price is null");
        }
    }

    /**
     * Рассчитывает сумму налога в платеже.
     *
     * @param productPrice стоимость товаров
     * @return НДС (10% от стоимости товаров), округленный до 2 знаков
     */
    private BigDecimal calculateTax(BigDecimal productPrice) {
        return productPrice.multiply(TAX_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);
    }
}