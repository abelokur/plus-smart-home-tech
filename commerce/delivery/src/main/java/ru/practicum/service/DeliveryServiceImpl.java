package ru.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.client.OrderClient;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.delivery.DeliveryState;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.practicum.exception.NoDeliveryFoundException;
import ru.practicum.mapper.DeliveryMapper;
import ru.practicum.model.Delivery;
import ru.practicum.repository.DeliveryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * Сервис для управления доставками заказов.
 * Обрабатывает бизнес-логику, связанную с планированием, отслеживанием
 * и расчетом стоимости доставок.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;
    private final TransactionTemplate transactionTemplate;

    private static final BigDecimal BASE_DELIVERY_RATE = BigDecimal.valueOf(5);

    /**
     * Создает или планирует новую доставку.
     *
     * @param deliveryDto данные доставки
     * @return созданная доставка в формате DTO
     */
    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDto);
        return deliveryMapper.toDto(deliveryRepository.save(delivery));
    }

    /**
     * Отмечает доставку как успешно завершенную.
     * Обновляет статус доставки и уведомляет сервис заказов.
     *
     * @param orderId идентификатор заказа
     */
    @Override
    public void successfulDelivery(UUID orderId) {
        transactionTemplate.executeWithoutResult(status -> {
            Delivery delivery = getDelivery(orderId);
            delivery.setDeliveryState(DeliveryState.DELIVERED);
        });

        try {
            orderClient.delivery(orderId);
        } catch (Exception e) {
            log.warn("Error while trying to send SUCCESS status to orderClient", e);
        }
    }

    /**
     * Отмечает, что доставка забрана курьером.
     * Обновляет статус доставки, уведомляет склад и сервис заказов.
     *
     * @param orderId идентификатор заказа
     */
    @Override
    public void pickedDelivery(UUID orderId) {
        Delivery delivery = transactionTemplate.execute(status -> {
            Delivery deliveryInProgress = getDelivery(orderId);
            deliveryInProgress.setDeliveryState(DeliveryState.IN_PROGRESS);
            return deliveryInProgress;
        });

        ShippedToDeliveryRequest request =
                new ShippedToDeliveryRequest(orderId, delivery.getDeliveryId());

        try {
            warehouseClient.shippedToDelivery(request);
        } catch (Exception e) {
            log.warn("Error while trying to send delivery request to warehouseClient", e);
        }

        try {
            orderClient.assemblyOrder(orderId);
        } catch (Exception e) {
            log.warn("Error while trying to set Assembled status to orderClient", e);
        }
    }

    /**
     * Отмечает доставку как неудачную.
     * Обновляет статус доставки и уведомляет сервис заказов.
     *
     * @param orderId идентификатор заказа
     */
    @Override
    public void failedDelivery(UUID orderId) {
        transactionTemplate.executeWithoutResult(status -> {
            Delivery delivery = getDelivery(orderId);
            delivery.setDeliveryState(DeliveryState.FAILED);
        });

        try {
            orderClient.deliveryFailed(orderId);
        } catch (Exception e) {
            log.warn("Error while trying to send FAILED status to orderClient", e);
        }
    }

    /**
     * Рассчитывает стоимость доставки на основе параметров заказа.
     * Формула расчета включает:
     * <ol>
     *   <li>Базовую ставку</li>
     *   <li>Множитель для определенного адреса склада</li>
     *   <li>Надбавку за хрупкость (20%)</li>
     *   <li>Стоимость по весу и объему</li>
     *   <li>Надбавку за разные улицы отправления и доставки (20%)</li>
     * </ol>
     *
     * @param orderDto данные заказа
     * @return рассчитанная стоимость доставки, округленная до 2 знаков
     */
    @Override
    public BigDecimal deliveryCost(OrderDto orderDto) {
        Delivery delivery = getDelivery(orderDto.orderId());
        String fromStreet = delivery.getFromAddress().getStreet();
        String toStreet = delivery.getToAddress().getStreet();

        BigDecimal cost = BASE_DELIVERY_RATE;

        // 1. Проверяем адрес склада, если адрес склада ADDRESS_2,
        // то базовую стоимость умножаем на 2 и прибавляем базовую стоимость
        // другими словами базовую стоимость умножаем на 3
        if (fromStreet.equalsIgnoreCase("ADDRESS_2")) {
            cost = cost.multiply(BigDecimal.valueOf(3));
        }

        // 2. Если хрупкий, до добавляем 20% к стоимости доставки
        if (orderDto.fragile()) {
            cost = cost.multiply(BigDecimal.valueOf(1.2));
        }

        // 3. Добавляем стоимость доставки по весу и объему
        BigDecimal weightCost = BigDecimal.valueOf(orderDto.deliveryWeight())
                .multiply(BigDecimal.valueOf(0.3));

        BigDecimal volumeCost = BigDecimal.valueOf(orderDto.deliveryVolume())
                .multiply(BigDecimal.valueOf(0.2));

        cost = cost.add(weightCost).add(volumeCost);

        // 4. если улица склада и улица доставки не совпадают то добавляем 20% к стоимости доставки
        if (!fromStreet.equalsIgnoreCase(toStreet)) {
            cost = cost.multiply(BigDecimal.valueOf(1.2));
        }

        return cost.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Отменяет доставку.
     * Обновляет статус доставки на CANCELLED.
     *
     * @param deliveryId идентификатор доставки
     * @throws NoDeliveryFoundException если доставка не найдена
     */
    @Override
    @Transactional
    public void cancelDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId)
                .orElseThrow(() ->
                        new NoDeliveryFoundException("Delivery for deliveryId = " + deliveryId + " not found"));

        if (delivery.getDeliveryState().equals(DeliveryState.CANCELLED)) {
            log.info("Delivery for deliveryId = " + deliveryId + " is already cancelled");
        }

        delivery.setDeliveryState(DeliveryState.CANCELLED);
    }

    /**
     * Находит доставку по идентификатору заказа.
     *
     * @param orderId идентификатор заказа
     * @return найденная доставка
     * @throws NoDeliveryFoundException если доставка не найдена
     */
    private Delivery getDelivery(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery for orderId = " + orderId + " not found"));
    }
}