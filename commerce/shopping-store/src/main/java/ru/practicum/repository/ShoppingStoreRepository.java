package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.model.Product;

import java.util.UUID;

/**
 * Репозиторий для работы с товарами магазина.
 */
public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {

    /**
     * Находит товары по категории с пагинацией.
     *
     * @param category категория товаров
     * @param pageable параметры пагинации
     * @return страница товаров
     */
    Page<Product> findAllByProductCategory(ProductCategory category, Pageable pageable);
}