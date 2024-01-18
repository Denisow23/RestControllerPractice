package ru.denisov.RestControllerPractice.repository.inmemory;


import ru.denisov.RestControllerPractice.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    List<Order> findAll();

    Optional<Order> findById(Long id);

    Order save(Order order);

    Order update(Order order);

    void deleteById(Long id);

    void deleteByIdIn(List<Long> ids);
}
