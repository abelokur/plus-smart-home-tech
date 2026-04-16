package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreServiceImp implements ShoppingStoreService {
    private final ShoppingStoreRepository shoppingStoreRepository;
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public ProductDto createProduct(ProductDto product) {
        Product productEntity = shoppingStoreRepository.save(productMapper.toEntity(product));
        return productMapper.toDto(productEntity);
    }

    @Transactional
    @Override
    public ProductDto updateProduct(ProductDto product) {
        Product productToUpdate = getProduct(product.productId());
        productMapper.updateEntity(product, productToUpdate);
        return productMapper.toDto(productToUpdate);
    }

    @Transactional
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

    @Transactional
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

    @Override
    public Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable) {
        return productMapper.toDto(shoppingStoreRepository.findAllByProductCategory(category, pageable));
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        return productMapper.toDto(getProduct(productId));
    }

    private Product getProduct(UUID productId) {
        return shoppingStoreRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(String.format("Product with id %s not found", productId)));
    }
}
