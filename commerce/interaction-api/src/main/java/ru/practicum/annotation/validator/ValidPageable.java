package ru.practicum.annotation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для валидации параметров пагинации {@link org.springframework.data.domain.Pageable}.
 * <p>
 * Применяется к параметрам методов для проверки корректности номеров страниц и их размера
 * в запросах с пагинацией. Валидатор обеспечивает, что значения находятся в заданных пределах.
 * <p>
 * <b>Значения по умолчанию:</b>
 * <ul>
 *   <li><b>minPage:</b> 0 (первая страница)</li>
 *   <li><b>maxPage:</b> {@link Integer#MAX_VALUE} (практически неограничено)</li>
 *   <li><b>minSize:</b> 1 (минимум один элемент на странице)</li>
 *   <li><b>maxSize:</b> 1000 (ограничение для защиты от чрезмерной нагрузки)</li>
 * </ul>
 * <p>
 * <b>Пример использования:</b>
 * <pre>{@code
 * @GetMapping("/items")
 * public ResponseEntity<Page<Item>> getItems(
 *         @ValidPageable(minSize = 1, maxSize = 50) Pageable pageable) {
 *     // возврат данных с пагинацией
 * }
 * }</pre>
 * <p>
 * <b>Важно:</b> Если в аннотации заданы некорректные значения (например, minPage > maxPage),
 * валидатор {@link PageableConstraintValidator} применяет защитную логику и использует
 * разумные значения по умолчанию.
 *
 * @see PageableConstraintValidator класс, реализующий логику валидации с защитой от некорректных настроек
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PageableConstraintValidator.class)
public @interface ValidPageable {

    /**
     * Сообщение об ошибке по умолчанию при нарушении валидации.
     *
     * @return сообщение об ошибке
     */
    String message() default "Invalid pagination parameters";

    /**
     * Группы ограничений для селективной валидации.
     *
     * @return массив классов групп
     */
    Class<?>[] groups() default {};

    /**
     * Дополнительные метаданные для валидации.
     *
     * @return массив классов полезной нагрузки
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Минимально допустимый номер страницы (включительно).
     * <p>
     * По умолчанию 0, что соответствует первой странице в Spring Data.
     * Отрицательные значения не рекомендуются.
     *
     * @return минимальный номер страницы
     */
    int minPage() default 0;

    /**
     * Максимально допустимый номер страницы (включительно).
     * <p>
     * По умолчанию {@link Integer#MAX_VALUE}, что практически снимает ограничение.
     * Установите разумное значение для защиты от глубокой пагинации.
     *
     * @return максимальный номер страницы
     */
    int maxPage() default Integer.MAX_VALUE;

    /**
     * Минимально допустимый размер страницы (включительно).
     * <p>
     * По умолчанию 1, так как страница должна содержать хотя бы один элемент.
     * Значение должно быть положительным.
     *
     * @return минимальный размер страницы
     */
    int minSize() default 1;

    /**
     * Максимально допустимый размер страницы (включительно).
     * <p>
     * По умолчанию 1000 для защиты от чрезмерной нагрузки на БД и память.
     * Рекомендуется устанавливать в зависимости от характеристик системы.
     *
     * @return максимальный размер страницы
     */
    int maxSize() default 1000;
}