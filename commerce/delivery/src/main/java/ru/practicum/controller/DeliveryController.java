package ru.practicum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.LogAllMethods;
import ru.practicum.client.DeliveryClient;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Контроллер для управления доставками заказов.
 * Обрабатывает операции планирования, отслеживания и управления доставками.
 */
@LogAllMethods
@RestController
@RequestMapping("/api/v1/delivery")
@Validated
@RequiredArgsConstructor
@Tag(name = "Delivery API", description = "Операции по управлению доставкой заказов")
public class DeliveryController implements DeliveryClient {
    private final DeliveryService deliveryService;

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Запланировать доставку",
            description = "Создает или обновляет информацию о доставке заказа"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Доставка успешно запланирована"),
            @ApiResponse(responseCode = "400", description = "Неверные данные доставки"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    public DeliveryDto planDelivery(
            @Parameter(description = "Данные доставки", required = true)
            @RequestBody @Valid DeliveryDto deliveryDto) {
        return deliveryService.planDelivery(deliveryDto);
    }

    @Override
    @PostMapping("/successful")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Отметить доставку как успешную",
            description = "Обновляет статус доставки заказа на 'доставлен'"
    )
    @ApiResponse(responseCode = "200", description = "Статус доставки обновлен")
    public void successfulDelivery(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        deliveryService.successfulDelivery(orderId);
    }

    @Override
    @PostMapping("/picked")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Отметить, что доставка забрана",
            description = "Отмечает, что курьер забрал заказ для доставки"
    )
    @ApiResponse(responseCode = "200", description = "Статус доставки обновлен")
    public void pickedDelivery(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        deliveryService.pickedDelivery(orderId);
    }

    @Override
    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Отметить доставку как неудачную",
            description = "Обновляет статус доставки заказа на 'не удалась'"
    )
    @ApiResponse(responseCode = "200", description = "Статус доставки обновлен")
    public void failedDelivery(
            @Parameter(description = "ID заказа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID orderId) {
        deliveryService.failedDelivery(orderId);
    }

    @Override
    @PostMapping("/cost")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Рассчитать стоимость доставки",
            description = "Вычисляет стоимость доставки на основе данных заказа"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Стоимость доставки рассчитана"),
            @ApiResponse(responseCode = "400", description = "Неверные данные заказа")
    })
    public BigDecimal deliveryCost(
            @Parameter(description = "Данные заказа для расчета", required = true)
            @RequestBody @Valid OrderDto orderDto) {
        return deliveryService.deliveryCost(orderDto);
    }

    @Override
    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Отменить доставку",
            description = "Отменяет запланированную доставку заказа"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Доставка отменена"),
            @ApiResponse(responseCode = "404", description = "Доставка не найдена")
    })
    public void cancelDelivery(
            @Parameter(description = "ID доставки", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID deliveryId) {
        deliveryService.cancelDelivery(deliveryId);
    }
}