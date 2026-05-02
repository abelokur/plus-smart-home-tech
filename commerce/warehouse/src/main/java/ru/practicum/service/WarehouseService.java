package ru.practicum.service;

import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

/**
 * Сервис для управления складом.
 * Определяет контракт для работы с запасами товаров на складе,
 * включая добавление, проверку наличия и управление бронированием товаров.
 */
public interface WarehouseService {

    /**
     * Добавляет новый тип товара на склад.
     * Регистрирует новый товар с указанием характеристик (вес, размеры, хрупкость).
     *
     * @param request данные о новом товаре
     */
    void addNewItem(NewProductInWarehouseRequest request);

    /**
     * Проверяет наличие товаров из корзины на складе.
     * Резервирует товары, если они доступны в достаточном количестве.
     *
     * @param shoppingCart корзина для проверки
     * @return информация о забронированных товарах
     */
    BookedProductsDto checkQuantityInWarehouse(ShoppingCartDto shoppingCart);

    /**
     * Добавляет количество существующего товара на склад.
     * Увеличивает остаток указанного товара на складе.
     *
     * @param request запрос на добавление товара
     */
    void addItem(AddProductToWarehouseRequest request);

    /**
     * Получает адрес склада.
     * Возвращает адрес основного или текущего склада.
     *
     * @return адрес склада
     */
    AddressDto getAddress();

    /**
     * Отмечает товары как отгруженные для доставки.
     * Обновляет статус товаров при передаче их в доставку.
     *
     * @param request данные об отгрузке
     */
    void shippedToDelivery(ShippedToDeliveryRequest request);

    /**
     * Возвращает товары на склад.
     * Увеличивает остаток товаров после отмены заказа или возврата.
     *
     * @param products товары для возврата (ID товара → количество)
     */
    void returnToWarehouse(Map<UUID, Long> products);

    /**
     * Собирает товары для заказа из корзины.
     * Подготавливает товары к отгрузке и возвращает их характеристики.
     *
     * @param request запрос на сборку товаров
     * @return информация о забронированных товарах с характеристиками доставки
     */
    BookedProductsDto assemblyProductForOrder(AssemblyProductsForOrderRequest request);

    /**
     * Отменяет сборку товаров для заказа.
     * Освобождает забронированные товары и возвращает их в доступный остаток.
     *
     * @param orderId идентификатор заказа
     */
    void cancelAssemblyProductForOrder(UUID orderId);
}