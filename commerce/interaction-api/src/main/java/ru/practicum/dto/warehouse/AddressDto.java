package ru.practicum.dto.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Объект передачи данных (DTO) представляющий адрес.
 * Содержит поля для страны, города, улицы, дома и квартиры.
 *
 * @param country страна адреса
 * @param city    город адреса
 * @param street  улица адреса
 * @param house   номер дома адреса
 * @param flat    номер квартиры адреса
 */
@Schema(description = "Адрес")
public record AddressDto(

        @Schema(
                description = "Страна",
                example = "Россия"
        )
        String country,

        @Schema(
                description = "Город",
                example = "Москва"
        )
        String city,

        @Schema(
                description = "Улица",
                example = "Тверская"
        )
        String street,

        @Schema(
                description = "Номер дома",
                example = "15"
        )
        String house,

        @Schema(
                description = "Номер квартиры",
                example = "42"
        )
        String flat
) {
}