package ru.practicum.service.mapper;

import org.apache.avro.generic.GenericEnumSymbol;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

public class EnumMapper {
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
