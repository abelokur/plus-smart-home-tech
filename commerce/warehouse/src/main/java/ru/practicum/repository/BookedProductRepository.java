package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.BookedProduct;

import java.util.UUID;

/**
 * Репозиторий для работы с забронированными товарами.
 */
public interface BookedProductRepository extends JpaRepository<BookedProduct, UUID> {
}