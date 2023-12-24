package ru.denisov.RestControllerPractice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import ru.denisov.RestControllerPractice.AbstractTestController;
import ru.denisov.RestControllerPractice.StringTestUtils;
import ru.denisov.RestControllerPractice.mapper.v1.ClientMapper;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.service.ClientService;
import ru.denisov.RestControllerPractice.web.model.ClientListResponse;
import ru.denisov.RestControllerPractice.web.model.ClientResponse;
import ru.denisov.RestControllerPractice.web.model.OrderResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertClientRequest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientControllerTest extends AbstractTestController {

    @MockBean
    private ClientService clientService;

    @MockBean
    private ClientMapper clientMapper;

    @Test
    public void whenFindAll_thenReturnClients() throws Exception {
        List<Client> clients = new ArrayList<>();
        clients.add(createClient(1L, null));
        Order order = createOrder(1L, 100L, null);
        clients.add(createClient(2L, order));

        List<ClientResponse> clientResponses = new ArrayList<>();
        clientResponses.add(createClientResponse(1L, null));
        OrderResponse orderResponse = createOrderResponse(1L, 100L);
        clientResponses.add(createClientResponse(2L, orderResponse));

        ClientListResponse clientListResponse = new ClientListResponse(clientResponses);

        Mockito.when(clientService.findAll()).thenReturn(clients);

        Mockito.when(clientMapper.clientListToClientResponseList(clients)).thenReturn(clientListResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/client"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_all_clients_response.json");

        Mockito.verify(clientService, Mockito.times(1)).findAll();
        Mockito.verify(clientMapper, Mockito.times(1)).clientListToClientResponseList(clients);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    }

    @Test
    public void whenGetClientById_thenReturnClientById() throws Exception {
        Client client = createClient(1L, null);
        ClientResponse clientResponse = createClientResponse(1L, null);

        Mockito.when(clientService.findById(1L)).thenReturn(client);
        Mockito.when(clientMapper.clientToResponse(client)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/client/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_client_by_id_response.json");

        Mockito.verify(clientService, Mockito.times(1)).findById(1L);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(client);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateClient_thenReturnNewClient() throws Exception {
        Client client = new Client();
        client.setName("Client 1");
        Client createdClient = createClient(1L, null);
        ClientResponse clientResponse = createClientResponse(1L, null);
        UpsertClientRequest request = new UpsertClientRequest("Client 1");

        Mockito.when(clientService.save(client)).thenReturn(createdClient);
        Mockito.when(clientMapper.requestToClient(request)).thenReturn(client);
        Mockito.when(clientMapper.clientToResponse(createdClient)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(post("/api/v1/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/create_client_response.json");

        Mockito.verify(clientService, Mockito.times(1)).save(client);
        Mockito.verify(clientMapper, Mockito.times(1)).requestToClient(request);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(createdClient);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdateClient_thenReturnUpdatedClient() throws Exception {
        UpsertClientRequest request = new UpsertClientRequest("New Client 1");
        Client updatedClient = new Client(1L, "New Client 1", new ArrayList<>());
        ClientResponse clientResponse = new ClientResponse(1L, "New Client 1", new ArrayList<>());

        Mockito.when(clientService.update(updatedClient)).thenReturn(updatedClient);
        Mockito.when(clientMapper.requestToClient(1L, request)).thenReturn(updatedClient);
        Mockito.when(clientMapper.clientToResponse(updatedClient)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(put("/api/v1/client/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/update_client_response.json");

        Mockito.verify(clientService, Mockito.times(1)).update(updatedClient);
        Mockito.verify(clientMapper, Mockito.times(1)).requestToClient(1L, request);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(updatedClient);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }



}
