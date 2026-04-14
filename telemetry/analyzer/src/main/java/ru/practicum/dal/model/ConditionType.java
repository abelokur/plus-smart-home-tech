package ru.practicum.dal.model;

/**
 * Типы условий для триггеров сценариев.
 */
public enum ConditionType {
    /**
     * Обнаружение движения.
     */
    MOTION,

    /**
     * Уровень освещенности.
     */
    LUMINOSITY,

    /**
     * Состояние переключателя (вкл/выкл).
     */
    SWITCH,

    /**
     * Температура.
     */
    TEMPERATURE,

    /**
     * Уровень CO2.
     */
    CO2LEVEL,

    /**
     * Влажность.
     */
    HUMIDITY
}
