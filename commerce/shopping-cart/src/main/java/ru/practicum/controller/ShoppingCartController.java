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
import ru.practicum.annotation.validator.ValidCartItems;
import ru.practicum.client.ShoppingCartClient;
import ru.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для управления корзиной покупок.
 * Обрабатывает операции получения, добавления, удаления товаров
 * и управления количеством товаров в корзине.
 */
@LogAllMethods
@RestController
@RequestMapping("/api/v1/shopping-cart")
@Validated
@RequiredArgsConstructor
@Tag(name = "Shopping Cart API", description = "Операции по управлению корзиной покупок")
public class ShoppingCartController implements ShoppingCartClient {
    private final ShoppingCartService shoppingCartService;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Получить корзину покупок",
            description = "Возвращает активную корзину покупок пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Корзина успешно получена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public ShoppingCartDto getShoppingCart(
            @Parameter(description = "Имя пользователя", required = true, example = "ivan_ivanov")
            @RequestParam String username) {
        return shoppingCartService.getShoppingCartByUsername(username);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Добавить товары в корзину",
            description = "Добавляет товары в корзину пользователя с суммированием количества"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товары успешно добавлены"),
            @ApiResponse(responseCode = "400", description = "Неверные данные товаров"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public ShoppingCartDto addItemToShoppingCart(
            @Parameter(description = "Имя пользователя", required = true, example = "ivan_ivanov")
            @RequestParam String username,

            @Parameter(description = "Товары для добавления (ID товара → количество)", required = true)
            @RequestBody @ValidCartItems Map<UUID, Long> items) {
        return shoppingCartService.addItemToShoppingCart(username, items);
    }

    @Override
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Деактивировать корзину",
            description = "Деактивирует (закрывает) корзину покупок пользователя"
    )
    @ApiResponse(responseCode = "200", description = "Корзина успешно деактивирована")
    public void deactivateShoppingCart(
            @Parameter(description = "Имя пользователя", required = true, example = "ivan_ivanov")
            @RequestParam String username) {
        shoppingCartService.deactivateShoppingCartByUsername(username);
    }

    @Override
    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Удалить товары из корзины",
            description = "Удаляет указанные товары из корзины пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Товары успешно удалены"),
            @ApiResponse(responseCode = "400", description = "Неверные данные товаров"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Товары не найдены в корзине")
    })
    public ShoppingCartDto removeItemFromShoppingCart(
            @Parameter(description = "Имя пользователя", required = true, example = "ivan_ivanov")
            @RequestParam String username,

            @Parameter(description = "Список ID товаров для удаления", required = true)
            @RequestBody List<UUID> items) {
        return shoppingCartService.removeItemFromShoppingCart(username, items);
    }

    @Override
    @PostMapping("/change-quantity")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Изменить количество товара",
            description = "Изменяет количество конкретного товара в корзине"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Количество успешно изменено"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Товар не найден в корзине")
    })
    public ShoppingCartDto changeItemQuantity(
            @Parameter(description = "Имя пользователя", required = true, example = "ivan_ivanov")
            @RequestParam String username,

            @Parameter(description = "Запрос на изменение количества", required = true)
            @RequestBody @Valid ChangeProductQuantityRequest request) {
        return shoppingCartService.changeItemQuantity(username, request);
    }
}