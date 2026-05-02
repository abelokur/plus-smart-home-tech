package ru.practicum.service.mapper;

import org.apache.avro.generic.GenericEnumSymbol;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

/**
 * Маппер Enum --> GenericEnumSymbol
 * Работает только с известными классами
 * (не универсальный, при добавлении новых типов Enum требуется их ручное добавление в switch)
 */
public class EnumMapper {
    /**
     * Маппит DeviceAction в DeviceActionAvro
     *
     * @param sourceEnum  объект Enum, может быть null
     * @param targetClass объект GenericEnumSymbol, не может быть null
     * @return GenericEnumSymbol, или null в случае если в параметры передан null
     */
    public static <T extends GenericEnumSymbol> T map(Enum<?> sourceEnum, Class<T> targetClass) {
        if (sourceEnum == null) {
            return null;
        }

        return (T) switch (targetClass) {
            case Class<?> clazz when clazz == ActionTypeAvro.class -> ActionTypeAvro.valueOf(sourceEnum.name());
            case Class<?> clazz when clazz == ConditionOperationAvro.class ->
                    ConditionOperationAvro.valueOf(sourceEnum.name());
            case Class<?> clazz when clazz == ConditionTypeAvro.class -> ConditionTypeAvro.valueOf(sourceEnum.name());
            case Class<?> clazz when clazz == DeviceTypeAvro.class -> DeviceTypeAvro.valueOf(sourceEnum.name());
            default -> throw new IllegalArgumentException("Unsupported target class: " + targetClass);
        };
    }
}
