package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Order;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностями Order.
 * Предоставляет методы для доступа к данным о заказах в базе данных.
 */
public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * Находит страницу заказов по имени пользователя с пагинацией.
     *
     * @param username имя пользователя
     * @param pageable параметры пагинации
     * @return страница с заказами пользователя
     */
    Page<Order> findByUsername(String username, Pageable pageable);
}