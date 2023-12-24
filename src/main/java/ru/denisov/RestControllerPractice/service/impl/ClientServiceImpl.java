package ru.denisov.RestControllerPractice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.repository.ClientRepository;
import ru.denisov.RestControllerPractice.repository.exception.EntityNotFoundException;
import ru.denisov.RestControllerPractice.service.ClientService;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Клиент с ID {} не найден",id)
                )
        );
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
        return clientRepository.update(client);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}
