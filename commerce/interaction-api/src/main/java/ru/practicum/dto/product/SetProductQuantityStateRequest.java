package ru.practicum.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Запрос на изменение состояния количества товара")
public class SetProductQuantityStateRequest {

    /**
     * ID товара.
     */
    @NotNull
    @Schema(
            description = "Идентификатор товара",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid"
    )
    private UUID productId;

    /**
     * Новое состояние количества.
     */
    @NotNull
    @Schema(
            description = "Новое состояние количества товара",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private QuantityState quantityState;
}