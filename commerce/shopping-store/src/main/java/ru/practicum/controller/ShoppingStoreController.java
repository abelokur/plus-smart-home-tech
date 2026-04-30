package ru.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.LogAllMethods;
import ru.practicum.annotation.validator.ValidPageable;
import ru.practicum.client.ShoppingStoreClient;
import ru.practicum.dto.product.ProductCategory;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.dto.product.SetProductQuantityStateRequest;
import ru.practicum.service.ShoppingStoreService;

import java.util.UUID;

/**
 * Контроллер для управления товарами магазина.
 * Обрабатывает операции получения, создания, обновления и удаления товаров.
 */
@LogAllMethods
@RestController
@RequestMapping("/api/v1/shopping-store")
@Validated
@RequiredArgsConstructor
@Tag(name = "Shopping Store API", description = "Операции по управлению товарами в магазине")
public class ShoppingStoreController implements ShoppingStoreClient {

    private final ShoppingStoreService shoppingStoreService;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Получить товары по категории",
            description = "Возвращает страницу товаров указанной категории с пагинацией"
    )
    @ApiResponse(responseCode = "200", description = "Товары успешно получены")
    public Page<ProductDto> getProducts(
            @Parameter(description = "Категория товаров", required = true)
            @RequestParam ProductCategory category,

            @Parameter(description = "Параметры пагинации и сортировки")
            @ValidPageable
            @PageableDefault(size = 20, sort = "productName", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return shoppingStoreService.getProductsByCategory(category, pageable);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Создать новый товар",
            description = "Добавляет новый товар в магазин"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные товара")
    })
    public ProductDto createProduct(
            @Parameter(description = "Данные нового товара", required = true)
            @RequestBody @Valid ProductDto dto) {
        return shoppingStoreService.createProduct(dto);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Обновить существующий товар",
            description = "Обновляет информацию о существующем товаре"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные товара"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    public ProductDto updateProduct(
            @Parameter(description = "Данные товара для обновления", required = true)
            @RequestBody @Valid ProductDto dto) {
        return shoppingStoreService.updateProduct(dto);
    }

    @Override
    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Удалить товар из магазина",
            description = "Удаляет товар из магазина (деактивирует)"
    )
    @ApiResponse(responseCode = "200", description = "Товар успешно удален")
    public Boolean removeProduct(
            @Parameter(description = "ID товара для удаления", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID productId) {
        return shoppingStoreService.removeProduct(productId);
    }

    @Override
    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Установить состояние количества товара",
            description = "Изменяет состояние количества товара на складе"
    )
    @ApiResponse(responseCode = "200", description = "Состояние количества успешно обновлено")
    public Boolean setQuantityState(
            @Parameter(description = "Запрос на изменение состояния количества", required = true)
            @Valid @ModelAttribute SetProductQuantityStateRequest request) {
        return shoppingStoreService.setQuantityState(request);
    }

    @Override
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Получить товар по ID",
            description = "Возвращает информацию о конкретном товаре по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар успешно найден"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    public ProductDto getProduct(
            @Parameter(description = "ID товара", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID productId) {
        return shoppingStoreService.getProductById(productId);
    }
}