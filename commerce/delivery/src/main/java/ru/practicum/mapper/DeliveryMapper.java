package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.delivery.DeliveryDto;
import ru.practicum.model.Delivery;

/**
 * Маппер для преобразования между сущностью Delivery и DTO.
 * Использует MapStruct для автоматической генерации кода преобразования.
 *
 * <p>Также использует AddressMapper для преобразования вложенных объектов адреса.</p>
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {AddressMapper.class})
public interface DeliveryMapper {

    /**
     * Преобразует сущность Delivery в DTO.
     *
     * @param delivery сущность доставки
     * @return DTO доставки
     */
    DeliveryDto toDto(Delivery delivery);

    /**
     * Преобразует DTO доставки в сущность.
     * Игнорирует поля createdAt и updatedAt, так как они управляются на уровне БД.
     *
     * @param deliveryDto DTO доставки
     * @return сущность доставки
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Delivery toEntity(DeliveryDto deliveryDto);
}