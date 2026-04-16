package ru.practicum.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;

/**
 * Валидатор для проверки параметров пагинации {@link Pageable}.
 * <p>
 * Обеспечивает проверку корректности номеров страниц и их размера с использованием
 * ограничений, заданных через аннотацию {@link ValidPageable}. При некорректных
 * значениях в аннотации применяются защитные значения по умолчанию.
 * <p>
 * <b>Логика инициализации:</b>
 * <ul>
 *   <li><b>minPage:</b> если аннотация содержит отрицательное значение, используется 0</li>
 *   <li><b>maxPage:</b> если значение ≤ minPage, используется {@link Integer#MAX_VALUE}</li>
 *   <li><b>minSize:</b> если значение ≤ 0, используется 1</li>
 *   <li><b>maxSize:</b> если значение ≤ minSize, используется 1000</li>
 * </ul>
 * <p>
 * <b>Особенности валидации:</b>
 * <ul>
 *   <li>Значение {@code null} считается валидным</li>
 *   <li>Для каждого нарушения создается отдельное сообщение об ошибке</li>
 *   <li>Ошибки привязываются к конкретным свойствам ("page" или "size")</li>
 * </ul>
 *
 * @see ValidPageable
 */
public class PageableConstraintValidator implements ConstraintValidator<ValidPageable, Pageable> {

    private int minPage;
    private int maxPage;
    private int minSize;
    private int maxSize;

    /**
     * Инициализирует валидатор значениями из аннотации с применением защитных значений.
     * <p>
     * Если аннотация содержит некорректные значения (отрицательные или противоречивые),
     * используются следующие значения по умолчанию:
     * <ul>
     *   <li>Минимальная страница: 0</li>
     *   <li>Максимальная страница: {@link Integer#MAX_VALUE}</li>
     *   <li>Минимальный размер: 1</li>
     *   <li>Максимальный размер: 1000</li>
     * </ul>
     *
     * @param constraintAnnotation аннотация, содержащая заданные ограничения
     */
    @Override
    public void initialize(ValidPageable constraintAnnotation) {
        this.minPage = constraintAnnotation.minPage() >= 0 ?
                constraintAnnotation.minPage() : 0;
        this.maxPage = constraintAnnotation.maxPage() > minPage ?
                constraintAnnotation.maxPage() : Integer.MAX_VALUE;
        this.minSize = constraintAnnotation.minSize() > 0 ?
                constraintAnnotation.minSize() : 1;
        this.maxSize = constraintAnnotation.maxSize() > minSize ?
                constraintAnnotation.maxSize() : 1000;
    }

    /**
     * Проверяет, соответствует ли объект {@link Pageable} заданным ограничениям.
     *
     * @param value   проверяемый объект {@link Pageable}; {@code null} считается валидным
     * @param context контекст для создания сообщений об ошибках валидации
     * @return {@code true} если:
     * <ul>
     *   <li>значение равно {@code null}, или</li>
     *   <li>номер страницы в диапазоне [{@code minPage}, {@code maxPage}], и</li>
     *   <li>размер страницы в диапазоне [{@code minSize}, {@code maxSize}]</li>
     * </ul>
     * {@code false} в противном случае
     */
    @Override
    public boolean isValid(Pageable value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean valid = true;

        if (value.getPageNumber() < minPage) {
            addViolation(context, "page",
                    String.format("Page number must be >= %d", minPage));
            valid = false;
        }

        if (value.getPageNumber() > maxPage) {
            addViolation(context, "page",
                    String.format("Page number must be <= %d", maxPage));
            valid = false;
        }

        if (value.getPageSize() < minSize) {
            addViolation(context, "size",
                    String.format("Page size must be >= %d", minSize));
            valid = false;
        }

        if (value.getPageSize() > maxSize) {
            addViolation(context, "size",
                    String.format("Page size must be <= %d", maxSize));
            valid = false;
        }

        return valid;
    }

    /**
     * Создает и добавляет сообщение о нарушении валидации для указанного свойства.
     * <p>
     * Метод отключает стандартное сообщение об ошибке и создает новое,
     * привязанное к конкретному свойству ({@code page} или {@code size}).
     *
     * @param context  контекст валидации для создания ограничения
     * @param property имя свойства, к которому относится ошибка
     * @param message  текст сообщения об ошибке
     */
    private void addViolation(ConstraintValidatorContext context,
                              String property, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(property)
                .addConstraintViolation();
    }
}