package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.payment.PaymentDto;
import ru.practicum.model.Payment;

/**
 * Маппер для преобразования между сущностью Payment и DTO.
 * Использует MapStruct для автоматической генерации кода преобразования.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    /**
     * Преобразует сущность Payment в DTO.
     *
     * @param payment сущность платежа
     * @return DTO платежа
     */
    PaymentDto toDto(Payment payment);
}