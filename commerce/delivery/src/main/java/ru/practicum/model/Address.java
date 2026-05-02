package ru.practicum.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

/**
 * Сущность адреса склада.
 * Хранит информацию о физическом адресе.
 */
@Entity
@Table(name = "addresses")
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Address {

    /**
     * Уникальный идентификатор адреса.
     */
    @Id
    @UuidGenerator
    @Column(name = "address_id", updatable = false, nullable = false)
    @Schema(description = "Уникальный идентификатор адреса", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID addressId;

    /**
     * Страна.
     */
    @Column(name = "country", length = 65)
    @Schema(description = "Страна", example = "Россия", maxLength = 65)
    private String country;

    /**
     * Город.
     */
    @Column(name = "city", length = 50)
    @Schema(description = "Город", example = "Москва", maxLength = 50)
    private String city;

    /**
     * Улица.
     */
    @Column(name = "street")
    @Schema(description = "Улица", example = "Тверская")
    private String street;

    /**
     * Дом.
     */
    @Column(name = "house", length = 20)
    @Schema(description = "Номер дома", example = "15", maxLength = 20)
    private String house;

    /**
     * Квартира/офис.
     */
    @Column(name = "flat", length = 20)
    @Schema(description = "Номер квартиры/офиса", example = "42", maxLength = 20)
    private String flat;
}