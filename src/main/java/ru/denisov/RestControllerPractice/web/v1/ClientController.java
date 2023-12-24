package ru.denisov.RestControllerPractice.web.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.denisov.RestControllerPractice.mapper.v1.ClientMapper;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.service.ClientService;
import ru.denisov.RestControllerPractice.web.model.ClientListResponse;
import ru.denisov.RestControllerPractice.web.model.ClientResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertClientRequest;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    private final ClientMapper clientMapper;

    @GetMapping
    public ResponseEntity<ClientListResponse> findAll(){
        return ResponseEntity.ok(
                clientMapper.clientListToClientResponseList(clientService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                clientMapper.clientToResponse(clientService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody UpsertClientRequest request) {
        Client newClient = clientService.save(clientMapper.requestToClient(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientMapper.clientToResponse(newClient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable("id") Long clientId
            , @RequestBody UpsertClientRequest request) {
        Client updatedClient = clientService.update(clientMapper.requestToClient(clientId, request));

        return ResponseEntity.ok(clientMapper.clientToResponse(updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
