package ru.denisov.RestControllerPractice.service.impl.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.denisov.RestControllerPractice.aop.Loggable;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.repository.database.DatabaseClientRepository;
import ru.denisov.RestControllerPractice.repository.database.DatabaseOrderRepository;
import ru.denisov.RestControllerPractice.repository.exception.EntityNotFoundException;
import ru.denisov.RestControllerPractice.repository.utils.BeanUtils;
import ru.denisov.RestControllerPractice.service.ClientService;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseClientService implements ClientService {

    private final DatabaseClientRepository clientRepository;

    private final DatabaseOrderRepository orderRepository;
    @Override
    @Loggable
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Клиент с ID {0} не найден!", id)
                ));
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
        Client existedClient = findById(client.getId());

        BeanUtils.copyNonNullProperties(client, existedClient);

        return clientRepository.save(client);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional
    @Loggable
    public Client saveWithOrders(Client client, List<Order> orders) {
        Client savedClient = clientRepository.save(client);

        if(true) throw new RuntimeException();

        for (Order order : orders) {
            order.setClient(savedClient);
            var savedOrder = orderRepository.save(order);
            savedClient.addOrder(savedOrder);
        }

        return savedClient;
    }
}
