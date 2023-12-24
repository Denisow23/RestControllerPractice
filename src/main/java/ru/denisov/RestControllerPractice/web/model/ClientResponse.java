package ru.denisov.RestControllerPractice.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {

    private Long id;

    private String name;

    private List<OrderResponse> orders = new ArrayList<>();
}
