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
import ru.practicum.client.PaymentClient;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Контроллер для управления платежами.
 * Обрабатывает операции создания платежей, расчета стоимости
 * и обработки статусов платежных транзакций.
 */
@LogAllMethods
@RestController
@RequestMapping("/api/v1/payment")
@Validated
@RequiredArgsConstructor
@Tag(name = "Payment API", description = "Операции по обработке платежей")
public class PaymentController implements PaymentClient {
    private final PaymentService paymentService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Создать платеж",
            description = "Инициирует платежную транзакцию для указанного заказа"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Платеж успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные заказа")
    })
    public PaymentDto payment(
            @Parameter(description = "Данные заказа для оплаты", required = true)
            @RequestBody @Valid OrderDto orderDto) {
        return paymentService.payment(orderDto);
    }

    @Override
    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Рассчитать общую стоимость",
            description = "Вычисляет полную стоимость заказа (товары + доставка + налоги)"
    )
    @ApiResponse(responseCode = "200", description = "Стоимость успешно рассчитана")
    public BigDecimal getTotalCost(
            @Parameter(description = "Данные заказа для расчета", required = true)
            @RequestBody @Valid OrderDto orderDto) {
        return paymentService.getTotalCost(orderDto);
    }

    @Override
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Обработать успешный платеж",
            description = "Отмечает платеж как успешный и уведомляет связанные сервисы"
    )
    @ApiResponse(responseCode = "200", description = "Платеж успешно обработан")
    public void refund(
            @Parameter(description = "ID платежа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID paymentId) {
        paymentService.refund(paymentId);
    }

    @Override
    @PostMapping("/productCost")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Рассчитать стоимость товаров",
            description = "Вычисляет стоимость только товаров в заказе (без доставки и налогов)"
    )
    @ApiResponse(responseCode = "200", description = "Стоимость товаров рассчитана")
    public BigDecimal productCost(
            @Parameter(description = "Данные заказа для расчета", required = true)
            @RequestBody @Valid OrderDto orderDto) {
        return paymentService.productCost(orderDto);
    }

    @Override
    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Обработать неудачный платеж",
            description = "Отмечает платеж как неудачный и уведомляет связанные сервисы"
    )
    @ApiResponse(responseCode = "200", description = "Неудачный платеж обработан")
    public void failed(
            @Parameter(description = "ID неудачного платежа", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestBody UUID paymentId) {
        paymentService.failed(paymentId);
    }
}