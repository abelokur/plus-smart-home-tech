package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.warehouse.DimensionDto;
import ru.practicum.model.Dimension;

/**
 * Маппер для преобразования объектов размеров.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DimensionMapper {

    /**
     * Преобразует сущность размеров в DTO.
     *
     * @param dimension сущность размеров
     * @return DTO размеров
     */
    DimensionDto toDto(Dimension dimension);

    /**
     * Преобразует DTO размеров в сущность.
     *
     * @param dimensionDto DTO размеров
     * @return сущность размеров
     */
    @Mapping(target = "dimensionId", ignore = true)
    Dimension toEntity(DimensionDto dimensionDto);
}