package ru.denisov.RestControllerPractice.web.mapper.v2;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.web.model.ClientListResponse;
import ru.denisov.RestControllerPractice.web.model.ClientResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertClientRequest;
import ru.denisov.RestControllerPractice.web.model.UpsertOrderRequest;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {OrderMapperV2.class})
public interface ClientMapperV2 {

    Client requestToClient(UpsertClientRequest request);

    @Mapping(source = "clientId", target = "id")
    Client requestToClient(Long clientId, UpsertClientRequest request);

    ClientResponse clientToResponse(Client client);

    default ClientListResponse clientListToClientResponseList(List<Client> clients) {
        ClientListResponse response = new ClientListResponse();

        response.setClients(clients.stream()
                .map(this::clientToResponse)
                .collect(Collectors.toList()));

        return response;
    }

}
