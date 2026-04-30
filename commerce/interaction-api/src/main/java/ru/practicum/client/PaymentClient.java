package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Контракт для работы с платежами.
 * Используется как для контроллера, так и для Feign-клиента.
 */
public interface PaymentClient {

    /**
     * Создать платеж для заказа.
     * Инициирует платежную транзакцию на основе данных заказа.
     *
     * @param orderDto данные заказа для оплаты
     * @return информация о созданном платеже
     */
    @PostMapping
    PaymentDto payment(@RequestBody @Valid OrderDto orderDto);

    /**
     * Получить общую стоимость заказа.
     * Рассчитывает полную сумму к оплате, включая товары и доставку.
     *
     * @param orderDto данные заказа для расчета
     * @return общая стоимость заказа
     */
    @PostMapping("/totalCost")
    BigDecimal getTotalCost(@RequestBody @Valid OrderDto orderDto);

    /**
     * Выполнить оплату.
     * Инициирует процедуру осуществления платежа по указанному идентификатору.
     * Примечание: endpoint назван "/refund", но выполняет операцию оплаты.
     *
     * @param paymentId идентификатор платежа для осуществления оплаты
     */
    @PostMapping("/refund")
    void refund(@RequestBody UUID paymentId);

    /**
     * Рассчитать стоимость товаров в заказе.
     * Вычисляет сумму только за товары, без учета стоимости доставки.
     *
     * @param orderDto данные заказа для расчета
     * @return стоимость товаров в заказе
     */
    @PostMapping("/productCost")
    BigDecimal productCost(@RequestBody @Valid OrderDto orderDto);

    /**
     * Обработать неудачный платеж.
     * Отмечает платеж как неудачный и выполняет соответствующие действия.
     *
     * @param paymentId идентификатор неудачного платежа
     */
    @PostMapping("/failed")
    void failed(@RequestBody UUID paymentId);
}