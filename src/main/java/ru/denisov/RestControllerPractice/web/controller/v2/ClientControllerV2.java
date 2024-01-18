package ru.denisov.RestControllerPractice.web.controller.v2;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.service.ClientService;
import ru.denisov.RestControllerPractice.web.mapper.v2.ClientMapperV2;
import ru.denisov.RestControllerPractice.web.model.ClientListResponse;
import ru.denisov.RestControllerPractice.web.model.ClientResponse;
import ru.denisov.RestControllerPractice.web.model.CreateClientWithOrderRequest;
import ru.denisov.RestControllerPractice.web.model.UpsertClientRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v2/client")
@RequiredArgsConstructor
public class ClientControllerV2 {

    private final ClientService databaseClientService;

    private final ClientMapperV2 clientMapper;

    @GetMapping
    public ResponseEntity<ClientListResponse> findAll(){
        return ResponseEntity.ok(
                clientMapper.clientListToClientResponseList(
                databaseClientService.findAll()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                clientMapper.clientToResponse(
                        databaseClientService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody @Valid UpsertClientRequest request) {
        Client newClient = databaseClientService.save(clientMapper.requestToClient(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientMapper.clientToResponse(newClient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable("id") Long clientId
            , @RequestBody UpsertClientRequest request) {
        Client updatedClient = databaseClientService.update(clientMapper.requestToClient(clientId, request));

        return ResponseEntity.ok(clientMapper.clientToResponse(updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        databaseClientService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/save-with-orders")
    public ResponseEntity<ClientResponse> createWithOrders(@RequestBody CreateClientWithOrderRequest request) {
        Client client = Client.builder().name(request.getName()).build();
        List<Order> ordersList = request.getOrders().stream()
                .map(orderRequest -> Order.builder()
                        .product(orderRequest.getProduct())
                        .cost(orderRequest.getCost())
                        .build())
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                clientMapper.clientToResponse(databaseClientService.saveWithOrders(client, ordersList))
        );
    }
}
