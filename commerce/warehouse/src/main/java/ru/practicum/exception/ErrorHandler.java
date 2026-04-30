package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.exception.ErrorResponse;

/**
 * Обработчик исключений для контроллера склада.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler extends BaseExceptionHandler {

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSpecifiedProductInWarehouseException(NoSpecifiedProductInWarehouseException e) {
        return handleGenericException(e, "Product not found in warehouse");
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSpecifiedProductAlreadyInWarehouseException(SpecifiedProductAlreadyInWarehouseException e) {
        return handleGenericException(e, "Product already exists in warehouse");
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductInShoppingCartLowQuantityInWarehouse(ProductInShoppingCartLowQuantityInWarehouse e) {
        return handleGenericException(e, "Insufficient stock in warehouse");
    }

    @ExceptionHandler(ProductInShoppingCartNotInWarehouse.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductInShoppingCartNotInWarehouse(ProductInShoppingCartNotInWarehouse e) {
        return handleGenericException(e, "Out of stock in warehouse");
    }
}