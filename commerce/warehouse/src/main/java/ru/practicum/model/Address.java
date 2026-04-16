package ru.practicum.model;

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
    private UUID addressId;

    /**
     * Страна.
     */
    @Column(name = "country", length = 65)
    private String country;

    /**
     * Город.
     */
    @Column(name = "city", length = 50)
    private String city;

    /**
     * Улица.
     */
    @Column(name = "street")
    private String street;

    /**
     * Дом.
     */
    @Column(name = "house", length = 20)
    private String house;

    /**
     * Квартира/офис.
     */
    @Column(name = "flat", length = 20)
    private String flat;
}