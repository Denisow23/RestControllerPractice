package ru.denisov.RestControllerPractice.service;

import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> findAll();

    Order findById(Long id);

    Order save(Order order);

    Order update(Order order);

    void deleteById(Long id);

    void deleteByIdIn(List<Long> ids);
}
