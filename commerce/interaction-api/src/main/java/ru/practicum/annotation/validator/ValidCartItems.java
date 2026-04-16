package ru.practicum.annotation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для валидации элементов корзины покупок.
 * <p>
 * Проверяет, что карта товаров корзины соответствует следующим требованиям:
 * <ul>
 *   <li>Не является {@code null} и не пуста</li>
 *   <li>Каждое значение (количество товара) не равно {@code null} и ≥ 1</li>
 * </ul>
 * <p>
 * Используется для параметров методов или полей класса, содержащих карту товаров,
 * где ключом является {@link java.util.UUID} товара, а значением - его количество.
 * <p>
 * <b>Пример использования:</b>
 * <pre>{@code
 * public void updateCart(@ValidCartItems Map<UUID, Long> items) {
 *     // бизнес-логика
 * }
 * }</pre>
 *
 * @see CartItemsValidator класс, реализующий логику валидации
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CartItemsValidator.class)
public @interface ValidCartItems {

    /**
     * Сообщение об ошибке по умолчанию при нарушении валидации.
     *
     * @return сообщение об ошибке
     */
    String message() default "Invalid cart items";

    /**
     * Группы ограничений, к которым принадлежит данное ограничение.
     * <p>
     * Позволяет группировать проверки для разных сценариев валидации.
     *
     * @return массив классов групп
     */
    Class<?>[] groups() default {};

    /**
     * Дополнительная информация о метаданных ограничения.
     * <p>
     * Обычно используется для передачи пользовательских объектов,
     * содержащих дополнительную информацию об ошибке.
     *
     * @return массив классов полезной нагрузки
     */
    Class<? extends Payload>[] payload() default {};
}