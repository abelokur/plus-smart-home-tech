package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.ProductState;
import ru.practicum.dto.product.SetProductQuantityStateRequest;
import ru.practicum.exception.ProductNotFoundException;
import ru.practicum.mapper.ProductMapper;
import ru.practicum.model.Product;
import ru.practicum.repository.ShoppingStoreRepository;

import java.util.UUID;

/**
 * Реализация сервиса управления товарами магазина.
 * Обрабатывает операции создания, обновления, удаления и получения товаров
 * с поддержкой кэширования для повышения производительности.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreServiceImp implements ShoppingStoreService {
    private final ShoppingStoreRepository shoppingStoreRepository;
    private final ProductMapper productMapper;

    /**
     * Создает новый товар в магазине.
     * Очищает кэш товаров после создания.
     *
     * @param product DTO нового товара
     * @return созданный товар
     */
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    @Override
    public ProductDto createProduct(ProductDto product) {
        Product productEntity = shoppingStoreRepository.save(productMapper.toEntity(product));
        return productMapper.toDto(productEntity);
    }

    /**
     * Обновляет существующий товар.
     * Очищает кэш товаров после обновления.
     *
     * @param product DTO с обновленными данными товара
     * @return обновленный товар
     * @throws ProductNotFoundException если товар не найден
     */
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    @Override
    public ProductDto updateProduct(ProductDto product) {
        Product productToUpdate = getProduct(product.productId());
        productMapper.updateEntity(product, productToUpdate);
        return productMapper.toDto(productToUpdate);
    }

    /**
     * Удаляет (деактивирует) товар из магазина.
     * Очищает кэш товаров после удаления.
     *
     * @param productId идентификатор товара
     * @return true, если удаление успешно, false в случае ошибки
     * @throws ProductNotFoundException если товар не найден
     */
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    @Override
    public Boolean removeProduct(UUID productId) {
        Product productToRemove = getProduct(productId);
        try {
            productToRemove.setProductState(ProductState.DEACTIVATE);
            return true;
        } catch (Exception e) {
            log.error("Error removing product", e);
            return false;
        }
    }

    /**
     * Устанавливает состояние количества для товара.
     * Очищает кэш товаров после обновления.
     *
     * @param request запрос на изменение состояния количества
     * @return true, если обновление успешно, false в случае ошибки
     * @throws ProductNotFoundException если товар не найден
     */
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    @Override
    public Boolean setQuantityState(SetProductQuantityStateRequest request) {
        Product productToSetQuantityState = getProduct(request.getProductId());
        try {
            productToSetQuantityState.setQuantityState(request.getQuantityState());
            return true;
        } catch (Exception e) {
            log.error("Error setting quantity state", e);
            return false;
        }
    }

    /**
     * Получает товары по категории с пагинацией.
     * Использует кэширование для повышения производительности.
     *
     * @param category категория товаров
     * @param pageable параметры пагинации
     * @return страница товаров указанной категории
     */
    @Cacheable(cacheNames = "products")
    @Override
    public Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable) {
        return productMapper.toDto(shoppingStoreRepository.findAllByProductCategory(category, pageable));
    }

    /**
     * Получает товар по идентификатору.
     * Использует кэширование для повышения производительности.
     *
     * @param productId идентификатор товара
     * @return DTO товара
     * @throws ProductNotFoundException если товар не найден
     */
    @Cacheable(cacheNames = "products")
    @Override
    public ProductDto getProductById(UUID productId) {
        return productMapper.toDto(getProduct(productId));
    }

    /**
     * Находит товар по идентификатору.
     *
     * @param productId идентификатор товара
     * @return сущность товара
     * @throws ProductNotFoundException если товар не найден
     */
    private Product getProduct(UUID productId) {
        return shoppingStoreRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(String.format("Product with id %s not found", productId)));
    }
}