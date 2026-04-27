package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.WarehouseFeignClient;
import ru.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.exception.NoProductsInShoppingCartException;
import ru.practicum.exception.NotAuthorizedUserException;
import ru.practicum.mapper.ShoppingCartMapper;
import ru.practicum.model.ShoppingCart;
import ru.practicum.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Реализация сервиса управления корзиной покупок.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseFeignClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCartByUsername(String username) {
        validateUsername(username);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(username); // получаем имеющуюся или создаем новую корзину для пользователя
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto addItemToShoppingCart(String username, Map<UUID, Long> products) {
        validateUsername(username);

        ShoppingCart shoppingCart = getOrCreateShoppingCart(username); // получаем имеющуюся или создаем новую корзину для пользователя

        // Суммирование количества
        products.forEach((productId, quantity) ->
                shoppingCart.getProducts().merge(productId, quantity, Long::sum)
        );

        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        BookedProductsDto bookedProductsDto = warehouseClient.checkQuantityInWarehouse(shoppingCartDto); // проверяем доступность товара на складе

        log.debug("Booked products: {}", bookedProductsDto);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public void deactivateShoppingCartByUsername(String username) {
        validateUsername(username);

        shoppingCartRepository.findByUsernameIgnoreCaseAndActiveTrue(username)
                .ifPresentOrElse(
                        shoppingCart -> {
                            shoppingCart.setActive(false);
                            log.debug("Deactivated shopping cart for user: {}", username);
                        },
                        () -> log.warn("No active shopping cart found for user: {}", username)
                );
    }

    @Transactional
    @Override
    public ShoppingCartDto removeItemFromShoppingCart(String username, List<UUID> items) {
        validateUsername(username);

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items list is empty");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameIgnoreCaseAndActiveTrue(username)
                .orElseThrow(() -> new NoProductsInShoppingCartException("No shopping cart for username = " + username));

        Map<UUID, Long> products = shoppingCart.getProducts();
        List<UUID> notFoundItems = items.stream()
                .filter(item -> !products.containsKey(item))
                .toList();

        if (!notFoundItems.isEmpty()) {
            throw new NoProductsInShoppingCartException("No products in shopping cart for items = " + notFoundItems);
        }

        items.forEach(products.keySet()::remove);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto changeItemQuantity(String username, ChangeProductQuantityRequest request) {
        validateUsername(username);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameIgnoreCaseAndActiveTrue(username)
                .orElseThrow(() ->
                        new NoProductsInShoppingCartException("No shopping cart for username = " + username));

        Map<UUID, Long> products = shoppingCart.getProducts();

        if (!products.containsKey(request.productId())) {
            throw new NoProductsInShoppingCartException("No products in shopping cart for productId = " +
                                                        request.productId());
        }

        products.put(request.productId(), request.newQuantity());

        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);

        warehouseClient.checkQuantityInWarehouse(shoppingCartDto); //проверяем доступность товара на складе

        return shoppingCartDto;
    }


    private void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Username cannot be null or empty");
        }
    }

    private ShoppingCart getOrCreateShoppingCart(String username) {
        return shoppingCartRepository.findByUsernameIgnoreCaseAndActiveTrue(username)
                .orElseGet(() -> shoppingCartRepository.save(ShoppingCart.builder()
                        .username(username)
                        .active(true)
                        .build()));
    }
}
