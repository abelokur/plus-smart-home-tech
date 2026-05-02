package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.validator.ValidPageable;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.SetProductQuantityStateRequest;

import java.util.UUID;

/**
 * Контракт для работы с магазином.
 * Используется как для контроллера, так и для Feign-клиента.
 */
public interface ShoppingStoreClient {

    /**
     * Получает товары по категории с пагинацией.
     *
     * @param category категория товаров
     * @param pageable параметры пагинации
     * @return страница с товарами
     */
    @GetMapping
    Page<ProductDto> getProducts(
            @RequestParam ProductCategory category,
            @ValidPageable
            @PageableDefault(size = 20, sort = "productName", direction = Sort.Direction.ASC)
            Pageable pageable);

    /**
     * Создает новый товар.
     *
     * @param dto данные товара
     * @return созданный товар
     */
    @PutMapping
    ProductDto createProduct(@RequestBody @Valid ProductDto dto);

    /**
     * Обновляет существующий товар.
     *
     * @param dto обновленные данные товара
     * @return обновленный товар
     */
    @PostMapping
    ProductDto updateProduct(@RequestBody @Valid ProductDto dto);

    /**
     * Удаляет товар из магазина.
     *
     * @param productId ID товара
     * @return true если удаление успешно
     */
    @PostMapping("/removeProductFromStore")
    Boolean removeProduct(@RequestBody UUID productId);

    /**
     * Устанавливает состояние количества товара (например, в наличии/нет).
     *
     * @param request запрос на изменение состояния
     * @return true если операция успешна
     */
    @PostMapping("/quantityState")
    Boolean setQuantityState(@Valid @SpringQueryMap SetProductQuantityStateRequest request);

    /**
     * Получает товар по ID.
     *
     * @param productId ID товара
     * @return данные товара
     */
    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);
}