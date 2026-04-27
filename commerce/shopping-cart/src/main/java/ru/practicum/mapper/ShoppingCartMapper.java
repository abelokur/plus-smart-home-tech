package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.cart.ShoppingCartDto;
import ru.practicum.model.ShoppingCart;

/**
 * Маппер для преобразования объектов корзины покупок.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {

    /**
     * Преобразует сущность {@link ShoppingCart} в DTO.
     *
     * @param shoppingCart сущность корзины
     * @return DTO корзины
     */
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}