package ru.denisov.RestControllerPractice.web.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.denisov.RestControllerPractice.web.mapper.v1.ClientMapper;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.service.ClientService;
import ru.denisov.RestControllerPractice.web.model.ClientListResponse;
import ru.denisov.RestControllerPractice.web.model.ClientResponse;
import ru.denisov.RestControllerPractice.web.model.ErrorResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertClientRequest;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
@Tag(name = "Client V1", description = "Client API version V1")
public class ClientController {

    private final ClientService clientService;

    private final ClientMapper clientMapper;

    @Operation(
            summary = "Get clients",
            description = "Get all clients",
            tags = {"client"}
    )
    @GetMapping
    public ResponseEntity<ClientListResponse> findAll(){
        return ResponseEntity.ok(
                clientMapper.clientListToClientResponseList(clientService.findAll())
        );
    }

    @Operation(
            summary = "Get client by id",
            description = "Get client by ID. Return id, name and list of orders",
            tags = {"client", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = ClientResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                clientMapper.clientToResponse(clientService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody @Valid UpsertClientRequest request) {
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

    @Operation(
            summary = "Delete client by ID",
            description = "Delete client by ID",
            tags = {"client", "id"}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Void> notFoundHandler(EntityNotFoundException ex) {
//        return ResponseEntity.notFound().build();
//    }
}
