package ru.denisov.RestControllerPractice.service;

import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.service.exception.UpdateStateException;
import ru.denisov.RestControllerPractice.web.model.OrderFilter;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public interface OrderService {

    List<Order> filterBy(OrderFilter filter);

    List<Order> findAll();

    Order findById(Long id);

    Order save(Order order);

    Order update(Order order);

    void deleteById(Long id);

    void deleteByIdIn(List<Long> ids);

    default void checkForUpdate(Long orderId) {
        Order currentOrder = findById(orderId);
        Instant now = Instant.now();

        Duration duration = Duration.between(currentOrder.getUpdateAt(), now);

        if (duration.getSeconds() > 5) {
            throw new UpdateStateException("Невозможно обновить заказ!");
        }
    }
}
