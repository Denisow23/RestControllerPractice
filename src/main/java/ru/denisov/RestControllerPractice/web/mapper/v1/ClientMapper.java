package ru.denisov.RestControllerPractice.web.mapper.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.web.model.ClientListResponse;
import ru.denisov.RestControllerPractice.web.model.ClientResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertClientRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final OrderMapper orderMapper;

    public Client requestToClient(UpsertClientRequest request) {
        Client client = new Client();

        client.setName(request.getName());

        return client;
    }

    public Client requestToClient(Long clientId, UpsertClientRequest request) {
        Client client = requestToClient(request);

        client.setId(clientId);

        return client;
    }

    public ClientResponse clientToResponse(Client client) {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(client.getId());
        clientResponse.setName(client.getName());
        clientResponse.setOrders(orderMapper.orderListToResponseList(client.getOrders()));

        return clientResponse;
    }

    public ClientListResponse clientListToClientResponseList(List<Client> clients) {
        ClientListResponse response = new ClientListResponse();

        response.setClients(clients.stream()
                .map(this::clientToResponse)
                .collect(Collectors.toList())
        );

        return response;
    }
}
