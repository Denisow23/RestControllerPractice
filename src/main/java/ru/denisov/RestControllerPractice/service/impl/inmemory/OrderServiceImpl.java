package ru.denisov.RestControllerPractice.service.impl.inmemory;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.repository.inmemory.OrderRepository;
import ru.denisov.RestControllerPractice.repository.exception.EntityNotFoundException;
import ru.denisov.RestControllerPractice.service.OrderService;
import ru.denisov.RestControllerPractice.web.model.OrderFilter;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> filterBy(OrderFilter filter) {
        throw new NotImplementedException();
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                MessageFormat.format("Заказ с ID {0} не найден!", id)
                        )
                );
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        checkForUpdate(order.getId());
        return orderRepository.update(order);
    }

    @Override
    public void deleteById(Long id) {
        Order currentOrder = findById(id);
        currentOrder.getClient().removeOrder(id);
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        orderRepository.deleteByIdIn(ids);
    }
}
