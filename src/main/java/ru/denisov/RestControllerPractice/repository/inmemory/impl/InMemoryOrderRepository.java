package ru.denisov.RestControllerPractice.repository.inmemory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.repository.inmemory.ClientRepository;
import ru.denisov.RestControllerPractice.repository.inmemory.OrderRepository;
import ru.denisov.RestControllerPractice.repository.exception.EntityNotFoundException;
import ru.denisov.RestControllerPractice.repository.utils.BeanUtils;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryOrderRepository implements OrderRepository {


    private ClientRepository clientRepository;

    private final Map<Long,Order> repository = new ConcurrentHashMap<>();

    private final AtomicLong currentId = new AtomicLong(1);


    @Override
    public List<Order> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Order save(Order order) {
        Long orderId = currentId.getAndIncrement();
        Long clientId = order.getClient().getId();
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден!")); // Зачем она здесь???

//        order.setClient(client); Зачем здесь везде сеттеры???
        order.setId(orderId);
        Instant now = Instant.now();
        order.setCreateAt(now);
        order.setUpdateAt(now);

        repository.put(orderId, order);

        client.addOrder(order);

        clientRepository.update(client);

        return order;
    }

    @Override
    public Order update(Order order) {
        Long orderId = order.getId();
        Instant now = Instant.now();
        Order currentOrder = repository.get(orderId);

        if(currentOrder == null){
            throw new EntityNotFoundException(
                    MessageFormat.format("Заказ по ID {0} не найден!", orderId)
            );
        }

        BeanUtils.copyNonNullProperties(order, currentOrder);

        currentOrder.setUpdateAt(now);
//        currentOrder.setId(orderId);

        repository.put(orderId, currentOrder);

        return currentOrder;
    }

    @Override
    public void deleteById(Long id) {
        Order order = repository.get(id);

        if (Objects.isNull(order)) {
            throw new EntityNotFoundException(
                    MessageFormat.format(
                            "Заказа с ID {0} не существует!", id
                    )
            );
        }

        repository.remove(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        ids.forEach(repository::remove);
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }
}
