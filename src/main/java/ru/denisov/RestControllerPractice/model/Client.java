package ru.denisov.RestControllerPractice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private Long id;

    private String name;

    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order){
        orders.add(order);
    }

    public void removeOrder(Long orderId){
        orders = orders.stream().filter(o -> !o.getId().equals(orderId)).collect(Collectors.toList());
    }
}
