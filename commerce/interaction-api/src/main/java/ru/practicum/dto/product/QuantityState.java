package ru.practicum.dto.product;

/**
 * Перечисление, представляющее состояние количества товара.
 * Возможные значения: ЗАКОНЧИЛСЬ, МАЛО, ДОСТАТОЧНО, МНОГО.
 */
public enum QuantityState {
    ENDED,    // Закончился
    FEW,      // Мало
    ENOUGH,   // Достаточно
    MANY      // Много
}
