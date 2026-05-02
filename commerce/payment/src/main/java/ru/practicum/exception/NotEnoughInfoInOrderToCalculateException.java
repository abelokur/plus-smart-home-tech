package ru.practicum.exception;

/**
 * Исключение, выбрасываемое когда в заказе недостаточно информации
 * для выполнения расчетов (стоимости доставки, общей суммы и т.д.).
 * Используется при отсутствии необходимых данных для вычислений.
 */
public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {
    public NotEnoughInfoInOrderToCalculateException(String message) {
        super(message);
    }
}
