package ru.denisov.RestControllerPractice.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.denisov.RestControllerPractice.validation.OrderFilterValid;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@OrderFilterValid
public class OrderFilter {

    private Integer pageSize;

    private Integer pageNumber;

    private String productName;

    private BigDecimal minCost;

    private BigDecimal maxCost;

    private Instant createdBefore;

    private Instant updatedBefore;

    private Long clientId;

}
