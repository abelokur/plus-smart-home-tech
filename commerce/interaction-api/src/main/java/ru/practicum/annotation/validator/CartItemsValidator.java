package ru.practicum.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;
import java.util.UUID;

/**
 * Валидатор для проверки корректности элементов корзины покупок.
 * Проверяет, что карта товаров не пуста и количество каждого товара корректно.
 * <p>
 * Ключами карты должны быть UUID товаров, значениями - их количество (не менее 1).
 */
public class CartItemsValidator implements ConstraintValidator<ValidCartItems, Map<UUID, Long>> {

    /**
     * Проверяет валидность карты товаров корзины.
     *
     * @param items   карта товаров, где ключ - UUID товара, значение - количество
     * @param context контекст для настройки сообщений об ошибках валидации
     * @return {@code true} если карта валидна:
     * - не равна {@code null} и не пуста
     * - все значения (количество товаров) не равны {@code null} и ≥ 1
     * {@code false} в противном случае
     * @see ValidCartItems аннотация, использующая этот валидатор
     */
    @Override
    public boolean isValid(Map<UUID, Long> items, ConstraintValidatorContext context) {
        if (items == null || items.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Items cannot be empty")
                    .addConstraintViolation();
            return false;
        }

        boolean valid = true;
        for (Map.Entry<UUID, Long> entry : items.entrySet()) {
            if (entry.getValue() == null || entry.getValue() < 1) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Quantity must be at least 1 for product: " + entry.getKey())
                        .addConstraintViolation();
                valid = false;
            }
        }
        return valid;
    }
}