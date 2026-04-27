package ru.practicum.mapper;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import ru.practicum.dto.product.ProductDto;
import ru.practicum.model.Product;

import java.util.List;

/**
 * Маппер для преобразования объектов товаров.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    /**
     * Преобразует сущность товара в DTO.
     *
     * @param product сущность товара
     * @return DTO товара
     */
    ProductDto toDto(Product product);

    /**
     * Преобразует список сущностей товаров в список DTO.
     *
     * @param products список сущностей товаров
     * @return список DTO товаров
     */
    List<ProductDto> toDto(List<Product> products);

    /**
     * Преобразует страницу сущностей товаров в страницу DTO.
     *
     * @param page страница сущностей товаров
     * @return страница DTO товаров
     */
    default Page<ProductDto> toDto(Page<Product> page) {
        return page.map(this::toDto);
    }

    /**
     * Преобразует DTO товара в сущность (для создания).
     *
     * @param productDto DTO товара
     * @return сущность товара
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductDto productDto);

    /**
     * Обновляет сущность товара из DTO (для частичного обновления).
     *
     * @param productDto DTO с обновленными данными
     * @param product    сущность для обновления
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProductDto productDto, @MappingTarget Product product);
}