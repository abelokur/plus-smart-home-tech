package ru.practicum.controller;

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
 */
@LogAllMethods
@RestController
@RequestMapping("/api/v1/shopping-store")
@Validated
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreClient {

    private final ShoppingStoreService shoppingStoreService;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getProducts(
            @RequestParam ProductCategory category,
            @ValidPageable
            @PageableDefault(size = 20, sort = "productName", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return shoppingStoreService.getProductsByCategory(category, pageable);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto createProduct(@RequestBody @Valid ProductDto dto) {
        return shoppingStoreService.createProduct(dto);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@RequestBody @Valid ProductDto dto) {
        return shoppingStoreService.updateProduct(dto);
    }

    @Override
    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public Boolean removeProduct(@RequestBody UUID productId) {
        return shoppingStoreService.removeProduct(productId);
    }

    @Override
    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public Boolean setQuantityState(@Valid @ModelAttribute SetProductQuantityStateRequest request) {
        return shoppingStoreService.setQuantityState(request);
    }

    @Override
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProduct(@PathVariable UUID productId) {
        return shoppingStoreService.getProductById(productId);
    }
}
