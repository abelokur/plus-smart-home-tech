package ru.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.SetProductQuantityStateRequest;

import java.util.UUID;

/**
 * Сервис для управления товарами магазина.
 */
public interface ShoppingStoreService {

    /**
     * Создает новый товар.
     *
     * @param product данные товара
     * @return созданный товар
     */
    ProductDto createProduct(ProductDto product);

    /**
     * Обновляет существующий товар.
     *
     * @param product обновленные данные товара
     * @return обновленный товар
     */
    ProductDto updateProduct(ProductDto product);

    /**
     * Удаляет товар.
     *
     * @param productId ID товара
     * @return true если удаление успешно
     */
    Boolean removeProduct(UUID productId);

    /**
     * Изменяет состояние количества товара.
     *
     * @param request запрос на изменение состояния
     * @return true если операция успешна
     */
    Boolean setQuantityState(SetProductQuantityStateRequest request);

    /**
     * Получает товары по категории с пагинацией.
     *
     * @param category категория товаров
     * @param pageable параметры пагинации
     * @return страница товаров
     */
    Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    /**
     * Получает товар по ID.
     *
     * @param productId ID товара
     * @return данные товара
     */
    ProductDto getProductById(UUID productId);
}