package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Delivery;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностями Delivery.
 * Предоставляет методы для доступа к данным о доставках в базе данных.
 */
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    /**
     * Находит доставку по идентификатору заказа.
     *
     * @param orderId идентификатор заказа
     * @return Optional с доставкой, если найдена, иначе пустой Optional
     */
    Optional<Delivery> findByOrderId(UUID orderId);

    /**
     * Находит доставку по её идентификатору.
     * Аналогичен стандартному методу findById, предоставлен для ясности API.
     *
     * @param deliveryId идентификатор доставки
     * @return Optional с доставкой, если найдена, иначе пустой Optional
     */
    Optional<Delivery> findByDeliveryId(UUID deliveryId);
}