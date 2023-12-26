package ru.denisov.RestControllerPractice.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertOrderRequest {


//    @NotNull(message = "ID клиента должно быть указано!")
//    @Positive(message = "ID клиента должно быть больше 0!")
    private Long clientId;

    @NotBlank(message = "Имя продукта должно быть указано!")
    private String product;

    @NotNull(message = "Цена должна быть указана!")
    @Positive(message = "Цена должна быть больше 0!")
    private BigDecimal cost;
}
