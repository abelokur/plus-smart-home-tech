package ru.practicum.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.AddressDto;

/**
 * Запрос на создание нового заказа.
 * Содержит корзину товаров и адрес доставки.
 */
@Schema(description = "Запрос на создание нового заказа")
public record CreateNewOrderRequest(
        @NotNull
        @Schema(
                description = "Корзина с товарами для заказа",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        ShoppingCartDto shoppingCart,

        @NotNull
        @Schema(
                description = "Адрес доставки заказа",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        AddressDto deliveryAddress
) {
}