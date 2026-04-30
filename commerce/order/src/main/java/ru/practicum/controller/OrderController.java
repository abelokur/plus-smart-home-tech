package ru.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.practicum.client.OrderClient;
import ru.practicum.dto.order.CreateNewOrderRequest;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.order.ProductReturnRequest;
import ru.practicum.service.OrderService;

import java.util.UUID;

/**
 * Контроллер для управления заказами.
 * Обрабатывает операции создания, получения и изменения статусов заказов.
 */
@LogAllMethods
@RestController
@RequestMapping("/api/v1/order")
@Validated
@RequiredArgsConstructor
@Tag(name = "Order API", description = "Операции по управлению заказами")
public class OrderController implements OrderClient {
    private final OrderService orderService;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Получить список заказов пользователя",
            description = "Возвращает заказы с пагинацией и сортировкой"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список заказов успешно получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public Page<OrderDto> getOrders(
            @Parameter(description = "Имя пользователя", required = true, example = "ivan_ivanov")
            @RequestParam String username,

            @Parameter(description = "Параметры пагинации и сортировки")
            @PageableDefault(size = 20, sort = "orderId", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return orderService.getOrders(username, pageable);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Создать новый заказ",
            description = "Создает заказ с резервированием товаров, планированием доставки и созданием платежа"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные заказа"),
            @ApiResponse(responseCode = "409", description = "Конфликт при создании заказа")
    })
    public OrderDto createOrder(
            @Parameter(description = "Данные для создания заказа", required = true)
            @RequestBody @Valid CreateNewOrderRequest request) {
        return orderService.createOrder(request);
    }

    @Override
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Вернуть товары из заказа",
            description = "Обрабатывает возврат товаров на склад и обновляет статус заказа"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возврат успешно обработан"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    public OrderDto returnOrder(
            @Parameter(description = "Данные для возврата товаров", required = true)
            @Valid @RequestBody ProductReturnRequest request) {
        return orderService.returnOrder(request);
    }

    @Override
    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Обработать успешную оплату",
            description = "Обновляет статус заказа после успешной оплаты"
    )
    @ApiResponse(responseCode = "200", description = "Статус заказа обновлен")
    public OrderDto paymentSuccess(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        return orderService.paymentSuccess(orderId);
    }

    @Override
    @PostMapping("/payment/failed")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Обработать неудачную оплату",
            description = "Обновляет статус заказа после неудачной оплаты"
    )
    @ApiResponse(responseCode = "200", description = "Статус заказа обновлен")
    public OrderDto paymentFailed(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        return orderService.paymentFailed(orderId);
    }

    @Override
    @PostMapping("/delivery")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Обработать успешную доставку",
            description = "Обновляет статус заказа после успешной доставки"
    )
    @ApiResponse(responseCode = "200", description = "Статус заказа обновлен")
    public OrderDto delivery(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        return orderService.deliveryOrder(orderId);
    }

    @Override
    @PostMapping("/delivery/failed")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Обработать неудачную доставку",
            description = "Обновляет статус заказа после неудачной доставки"
    )
    @ApiResponse(responseCode = "200", description = "Статус заказа обновлен")
    public OrderDto deliveryFailed(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        return orderService.deliveryFailed(orderId);
    }

    @Override
    @PostMapping("/completed")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Отметить заказ как завершенный",
            description = "Обновляет статус заказа на 'завершен'"
    )
    @ApiResponse(responseCode = "200", description = "Статус заказа обновлен")
    public OrderDto completedOrder(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        return orderService.completedOrder(orderId);
    }

    @Override
    @PostMapping("/calculate/total")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Рассчитать общую стоимость заказа",
            description = "Вычисляет полную стоимость заказа (товары + доставка)"
    )
    @ApiResponse(responseCode = "200", description = "Стоимость рассчитана")
    public OrderDto calculateTotal(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        return orderService.calculateTotal(orderId);
    }

    @Override
    @PostMapping("/calculate/delivery")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Рассчитать стоимость доставки",
            description = "Вычисляет стоимость доставки для заказа"
    )
    @ApiResponse(responseCode = "200", description = "Стоимость доставки рассчитана")
    public OrderDto calculateDelivery(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        return orderService.calculateDelivery(orderId);
    }

    @Override
    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Отметить заказ как собранный",
            description = "Обновляет статус заказа на 'собран'"
    )
    @ApiResponse(responseCode = "200", description = "Статус заказа обновлен")
    public OrderDto assemblyOrder(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        return orderService.assemblyOrder(orderId);
    }

    @Override
    @PostMapping("/assembly/failed")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Обработать неудачную сборку",
            description = "Обновляет статус заказа на 'сборка не удалась'"
    )
    @ApiResponse(responseCode = "200", description = "Статус заказа обновлен")
    public OrderDto assemblyFailed(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        return orderService.assemblyFailed(orderId);
    }
}