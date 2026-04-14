package ru.practicum.dal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * Сущность сенсора умного дома.
 * Хранится в таблице "sensors" базы данных.
 * Сенсор представляет физическое устройство, которое может измерять параметры
 * или выполнять действия в системе умного дома.
 */
@Entity
@Table(name = "sensors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {

    /**
     * Уникальный идентификатор сенсора.
     * Используется как первичный ключ, не может быть null и должен быть уникальным.
     */
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    /**
     * Идентификатор хаба, к которому привязан сенсор.
     * Обязательное поле.
     */
    @Column(name = "hub_id", nullable = false)
    private String hubId;

    //Методы HashCode и Equals переопределены для корректной работы с Map, где сенсор выступает ключом.

    /**
     * Сравнение сенсоров по идентификатору с учетом Hibernate proxy.
     *
     * @param object объект для сравнения
     * @return true если сенсоры имеют одинаковый идентификатор
     */
    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Sensor sensor = (Sensor) object;
        return getId() != null && Objects.equals(getId(), sensor.getId());
    }

    /**
     * Хэш-код сенсора на основе класса для совместимости с Hibernate proxy.
     *
     * @return хэш-код класса сенсора
     */
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
