package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.warehouse.AddressDto;
import ru.practicum.model.Address;

/**
 * Маппер для преобразования объектов адреса.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    /**
     * Преобразует сущность адреса в DTO.
     *
     * @param address сущность адреса
     * @return DTO адреса
     */
    AddressDto toDto(Address address);

    /**
     * Преобразует DTO адреса в сущность.
     *
     * @param addressDto DTO адреса
     * @return сущность адреса
     */
    @Mapping(target = "addressId", ignore = true)
    Address toEntity(AddressDto addressDto);
}