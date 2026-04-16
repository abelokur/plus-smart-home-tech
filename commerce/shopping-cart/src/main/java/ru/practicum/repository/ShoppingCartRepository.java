package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с корзинами покупок.
 */
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    /**
     * Находит активную корзину по имени пользователя.
     *
     * @param username имя пользователя
     * @return корзина пользователя
     */
    Optional<ShoppingCart> findByUsernameIgnoreCaseAndActiveTrue(String username);
}