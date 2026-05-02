package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import ru.practicum.dto.order.OrderDto;
import ru.practicum.model.Order;

/**
 * Маппер для преобразования между сущностью Order и DTO.
 * Использует MapStruct для автоматической генерации кода преобразования.
 * Также предоставляет метод для преобразования страниц с заказами.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    /**
     * Преобразует сущность Order в DTO.
     *
     * @param order сущность заказа
     * @return DTO заказа
     */
    OrderDto toDto(Order order);

    /**
     * Преобразует страницу сущностей Order в страницу DTO.
     * Используется для пагинированных ответов API.
     *
     * @param page страница с сущностями заказов
     * @return страница с DTO заказов
     */
    default Page<OrderDto> toDto(Page<Order> page) {
        return page.map(this::toDto);
    }
}