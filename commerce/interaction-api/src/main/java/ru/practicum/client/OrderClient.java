package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.validator.ValidPageable;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.ProductReturnRequest;

import java.util.UUID;

/**
 * Контракт для работы с заказами.
 * Используется как для контроллера, так и для Feign-клиента.
 */
public interface OrderClient {

    /**
     * Получить список заказов пользователя с пагинацией.
     *
     * @param username имя пользователя
     * @param pageable параметры пагинации и сортировки
     * @return страница с заказами
     */
    @GetMapping
    Page<OrderDto> getOrders(
            @RequestParam String username,
            @ValidPageable
            @PageableDefault(size = 20, sort = "orderId", direction = Sort.Direction.DESC)
            Pageable pageable);

    /**
     * Создать новый заказ.
     *
     * @param request данные для создания заказа
     * @return созданный заказ
     */
    @PutMapping
    OrderDto createOrder(@RequestBody @Valid CreateNewOrderRequest request);

    /**
     * Вернуть товары из заказа.
     *
     * @param request данные для возврата товаров
     * @return обновленный заказ
     */
    @PostMapping("/return")
    OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest request);

    /**
     * Обработать успешную оплату заказа.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @PostMapping("/payment")
    OrderDto paymentSuccess(@RequestBody UUID orderId);

    /**
     * Обработать неудачную оплату заказа.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId);

    /**
     * Обработать успешную доставку заказа.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @PostMapping("/delivery")
    OrderDto delivery(@RequestBody UUID orderId);

    /**
     * Обработать неудачную доставку заказа.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @PostMapping("/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderId);

    /**
     * Отметить заказ как завершенный.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @PostMapping("/completed")
    OrderDto completedOrder(@RequestBody UUID orderId);

    /**
     * Рассчитать итоговую сумму заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ с рассчитанной суммой
     */
    @PostMapping("/calculate/total")
    OrderDto calculateTotal(@RequestBody UUID orderId);

    /**
     * Рассчитать стоимость доставки для заказа.
     *
     * @param orderId идентификатор заказа
     * @return заказ с рассчитанной доставкой
     */
    @PostMapping("/calculate/delivery")
    OrderDto calculateDelivery(@RequestBody UUID orderId);

    /**
     * Отметить заказ как собранный.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @PostMapping("/assembly")
    OrderDto assemblyOrder(@RequestBody UUID orderId);

    /**
     * Обработать неудачную сборку заказа.
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @PostMapping("/assembly/failed")
    OrderDto assemblyFailed(@RequestBody UUID orderId);
}