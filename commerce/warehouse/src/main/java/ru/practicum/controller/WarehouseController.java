package ru.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.LogAllMethods;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.*;
import ru.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для управления складом.
 * Обрабатывает операции добавления товаров, проверки наличия,
 * сборки заказов и управления запасами на складе.
 */
@LogAllMethods
@RestController
@RequestMapping("/api/v1/warehouse")
@Validated
@RequiredArgsConstructor
@Tag(name = "Warehouse API", description = "Операции по управлению складом и запасами")
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Добавить новый товар на склад",
            description = "Регистрирует новый тип товара на складе с указанием характеристик"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товар успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные товара")
    })
    public void addNewItemToWarehouse(
            @Parameter(description = "Данные нового товара", required = true)
            @RequestBody @Valid NewProductInWarehouseRequest request) {
        warehouseService.addNewItem(request);
    }

    @Override
    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Проверить наличие товаров на складе",
            description = "Проверяет доступность товаров из корзины"
    )
    @ApiResponse(responseCode = "200", description = "Проверка наличия выполнена")
    public BookedProductsDto checkQuantityInWarehouse(
            @Parameter(description = "Корзина для проверки", required = true)
            @RequestBody @Valid ShoppingCartDto shoppingCart) {
        return warehouseService.checkQuantityInWarehouse(shoppingCart);
    }

    @Override
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Добавить количество существующего товара",
            description = "Увеличивает количество существующего товара на складе"
    )
    @ApiResponse(responseCode = "200", description = "Количество товара увеличено")
    public void addItemToWarehouse(
            @Parameter(description = "Запрос на добавление товара", required = true)
            @RequestBody @Valid AddProductToWarehouseRequest request) {
        warehouseService.addItem(request);
    }

    @Override
    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Получить адрес склада",
            description = "Возвращает адрес основного склада"
    )
    @ApiResponse(responseCode = "200", description = "Адрес склада получен")
    public AddressDto getWarehouseAddress() {
        return warehouseService.getAddress();
    }

    @Override
    @PostMapping("/shipped")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Отметить товары как отгруженные",
            description = "Отмечает товары как отгруженные для доставки"
    )
    @ApiResponse(responseCode = "200", description = "Товары отмечены как отгруженные")
    public void shippedToDelivery(
            @Parameter(description = "Данные об отгрузке", required = true)
            @RequestBody @Valid ShippedToDeliveryRequest request) {
        warehouseService.shippedToDelivery(request);
    }

    @Override
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Вернуть товары на склад",
            description = "Возвращает товары на склад после отмены или возврата"
    )
    @ApiResponse(responseCode = "200", description = "Товары возвращены на склад")
    public void returnToWarehouse(
            @Parameter(description = "Товары для возврата (ID товара → количество)", required = true)
            @RequestBody Map<UUID, Long> products) {
        warehouseService.returnToWarehouse(products);
    }

    @Override
    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Собрать товары для заказа",
            description = "Собирает товары из корзины для формирования заказа"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товары собраны для заказа"),
            @ApiResponse(responseCode = "400", description = "Недостаточно товаров на складе")
    })
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(
            @Parameter(description = "Запрос на сборку товаров", required = true)
            @RequestBody @Valid AssemblyProductsForOrderRequest request) {
        return warehouseService.assemblyProductForOrder(request);
    }

    @Override
    @PostMapping("/assembly/cancel")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Отменить сборку товаров для заказа",
            description = "Отменяет сборку товаров для указанного заказа"
    )
    @ApiResponse(responseCode = "200", description = "Сборка товаров отменена")
    public void cancelAssemblyProductForOrder(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        warehouseService.cancelAssemblyProductForOrder(orderId);
    }
}