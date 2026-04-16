package ru.practicum.service;

import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.dto.warehouse.BookedProductsDto;
import ru.practicum.dto.warehouse.NewProductInWarehouseRequest;

/**
 * Сервис для управления складом.
 */
public interface WarehouseService {

    /**
     * Добавляет новый тип товара на склад.
     *
     * @param request данные о новом товаре
     */
    void addNewItem(NewProductInWarehouseRequest request);

    /**
     * Проверяет наличие товаров из корзины на складе.
     *
     * @param shoppingCart корзина для проверки
     * @return информация о забронированных товарах
     */
    BookedProductsDto checkQuantityInWarehouse(ShoppingCartDto shoppingCart);

    /**
     * Добавляет количество существующего товара на склад.
     *
     * @param request запрос на добавление товара
     */
    void addItem(AddProductToWarehouseRequest request);

    /**
     * Получает адрес склада.
     *
     * @return адрес склада
     */
    AddressDto getAddress();

    /**
     * Бронирует товары из корзины (тренировочный метод).
     *
     * @param shoppingCart корзина с товарами для бронирования
     */
    void bookProducts(ShoppingCartDto shoppingCart);
}