package ru.practicum.controller;

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
 */
@LogAllMethods
@RestController
@RequestMapping("/api/v1/shopping-cart")
@Validated
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartClient {
    private final ShoppingCartService shoppingCartService;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        return shoppingCartService.getShoppingCartByUsername(username);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto addItemToShoppingCart(
            @RequestParam String username,
            @RequestBody @ValidCartItems Map<UUID, Long> items) {
        return shoppingCartService.addItemToShoppingCart(username, items);
    }

    @Override
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deactivateShoppingCart(@RequestParam String username) {
        shoppingCartService.deactivateShoppingCartByUsername(username);
    }

    @Override
    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeItemFromShoppingCart(@RequestParam String username, @RequestBody List<UUID> items) {
        return shoppingCartService.removeItemFromShoppingCart(username, items);
    }

    @Override
    @PostMapping("/change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto changeItemQuantity(
            @RequestParam String username,
            @RequestBody @Valid ChangeProductQuantityRequest request) {
        return shoppingCartService.changeItemQuantity(username, request);
    }

}
