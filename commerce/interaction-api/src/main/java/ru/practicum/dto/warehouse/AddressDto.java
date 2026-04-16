package ru.practicum.dto.warehouse;

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
public record AddressDto(
        String country,
        String city,
        String street,
        String house,
        String flat
) {
}
