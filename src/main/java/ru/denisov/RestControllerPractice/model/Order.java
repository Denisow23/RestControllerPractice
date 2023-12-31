package ru.denisov.RestControllerPractice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private Long id;

    private String product;

    private BigDecimal cost;

    private Client client;

    private Instant createAt;

    private Instant updateAt;

}
