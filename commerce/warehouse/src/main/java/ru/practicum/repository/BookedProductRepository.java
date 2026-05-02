package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.BookedProduct;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с забронированными товарами.
 * Предоставляет методы для доступа к данным о забронированных товарах в базе данных.
 */
public interface BookedProductRepository extends JpaRepository<BookedProduct, UUID> {

    /**
     * Находит все забронированные товары для указанного заказа.
     *
     * @param orderId идентификатор заказа
     * @return список забронированных товаров для заказа
     */
    List<BookedProduct> findAllByOrderId(UUID orderId);

    /**
     * Обновляет количество забронированных товаров для заказа.
     * Выполняет массовое обновление в базе данных.
     *
     * @param orderId     идентификатор заказа
     * @param newQuantity новое количество товаров
     * @return количество обновленных записей
     */
    @Modifying
    @Query("UPDATE BookedProduct bp SET bp.quantity = :newQuantity WHERE bp.orderId = :orderId")
    int updateQuantity(@Param("orderId") UUID orderId, @Param("newQuantity") Long newQuantity);
}