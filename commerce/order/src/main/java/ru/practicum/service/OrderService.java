package ru.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.ProductReturnRequest;

import java.util.UUID;

/**
 * Сервис для управления заказами.
 * Определяет контракт для работы с заказами, включая создание,
 * обработку статусов и взаимодействие с другими сервисами.
 */
public interface OrderService {

    /**
     * Получает список заказов пользователя с пагинацией.
     *
     * @param username имя пользователя
     * @param pageable параметры пагинации
     * @return страница с заказами пользователя
     */
    Page<OrderDto> getOrders(String username, Pageable pageable);

    /**
     * Создает новый заказ.
     * Координирует взаимодействие со складом, доставкой и платежами.
     *
     * @param request данные для создания заказа
     * @return созданный заказ
     */
    OrderDto createOrder(CreateNewOrderRequest request);

    /**
     * Обрабатывает возврат товаров из заказа.
     * Возвращает товары на склад и обновляет статус заказа.
     *
     * @param request данные для возврата товаров
     * @return обновленный заказ
     */
    OrderDto returnOrder(ProductReturnRequest request);

    /**
     * Обрабатывает успешную оплату заказа.
     * Вызывается сервисом платежей.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    OrderDto paymentSuccess(UUID orderId);

    /**
     * Обрабатывает неудачную оплату заказа.
     * Вызывается сервисом платежей.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    OrderDto paymentFailed(UUID orderId);

    /**
     * Обрабатывает успешную доставку заказа.
     * Вызывается сервисом доставки.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    OrderDto deliveryOrder(UUID orderId);

    /**
     * Обрабатывает неудачную доставку заказа.
     * Вызывается сервисом доставки.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    OrderDto deliveryFailed(UUID orderId);

    /**
     * Отмечает заказ как завершенный.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    OrderDto completedOrder(UUID orderId);

    /**
     * Рассчитывает общую стоимость заказа.
     * Запрашивает расчет у сервиса платежей.
     *
     * @param orderId идентификатор заказа
     * @return заказ с рассчитанной стоимостью
     */
    OrderDto calculateTotal(UUID orderId);

    /**
     * Рассчитывает стоимость доставки для заказа.
     * Запрашивает расчет у сервиса доставки.
     *
     * @param orderId идентификатор заказа
     * @return заказ с рассчитанной стоимостью доставки
     */
    OrderDto calculateDelivery(UUID orderId);

    /**
     * Отмечает заказ как собранный.
     * Вызывается сервисом доставки.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    OrderDto assemblyOrder(UUID orderId);

    /**
     * Обрабатывает неудачную сборку заказа.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    OrderDto assemblyFailed(UUID orderId);
}