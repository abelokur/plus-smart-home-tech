package ru.practicum.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Запрос на изменение состояния количества товара.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetProductQuantityStateRequest {

    /**
     * ID товара.
     */
    @NotNull
    private UUID productId;

    /**
     * Новое состояние количества.
     */
    @NotNull
    private QuantityState quantityState;
}