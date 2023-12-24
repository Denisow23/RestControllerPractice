package ru.denisov.RestControllerPractice.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;

    private String product;

    private BigDecimal cost;

}
