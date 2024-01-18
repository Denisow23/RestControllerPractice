package ru.denisov.RestControllerPractice.web.controller.v2;

import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import ru.denisov.RestControllerPractice.AbstractTestController;
import ru.denisov.RestControllerPractice.StringTestUtils;
import ru.denisov.RestControllerPractice.service.impl.database.DatabaseClientService;
import ru.denisov.RestControllerPractice.service.impl.inmemory.ClientServiceImpl;
import ru.denisov.RestControllerPractice.web.mapper.v1.ClientMapper;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.repository.exception.EntityNotFoundException;
import ru.denisov.RestControllerPractice.web.mapper.v2.ClientMapperV2;
import ru.denisov.RestControllerPractice.web.model.ClientListResponse;
import ru.denisov.RestControllerPractice.web.model.ClientResponse;
import ru.denisov.RestControllerPractice.web.model.OrderResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertClientRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientControllerV2Test extends AbstractTestController {

    @MockBean
    private DatabaseClientService clientService;

    @MockBean
    private ClientMapperV2 clientMapper;

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

        String actualResponse = mockMvc.perform(get("/api/v2/client"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/ClientControllerTest/find_all_clients_response.json");

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

        String actualResponse = mockMvc.perform(get("/api/v2/client/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/ClientControllerTest/find_client_by_id_response.json");

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

        Mockito.when(clientMapper.requestToClient(request)).thenReturn(client);
        Mockito.when(clientService.save(client)).thenReturn(createdClient);
        Mockito.when(clientMapper.clientToResponse(createdClient)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(post("/api/v2/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/ClientControllerTest/create_client_response.json");

        Mockito.verify(clientMapper, Mockito.times(1)).requestToClient(request);
        Mockito.verify(clientService, Mockito.times(1)).save(client);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(createdClient);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdateClient_thenReturnUpdatedClient() throws Exception {
        UpsertClientRequest request = new UpsertClientRequest("New Client 1");
        Client updatedClient = new Client(1L, "New Client 1", new ArrayList<>());
        ClientResponse clientResponse = new ClientResponse(1L, "New Client 1", new ArrayList<>());

        Mockito.when(clientMapper.requestToClient(1L, request)).thenReturn(updatedClient);
        Mockito.when(clientService.update(updatedClient)).thenReturn(updatedClient);
        Mockito.when(clientMapper.clientToResponse(updatedClient)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(put("/api/v2/client/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/ClientControllerTest/update_client_response.json");

        Mockito.verify(clientService, Mockito.times(1)).update(updatedClient);
        Mockito.verify(clientMapper, Mockito.times(1)).requestToClient(1L, request);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(updatedClient);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteClientById_thenReturnStatusNoContent() throws Exception{
        mockMvc.perform(delete("/api/v2/client/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(clientService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void whenFindByIdNotExistedClient_thenReturnError() throws Exception {
        Mockito.when(clientService.findById(500L))
                .thenThrow(new EntityNotFoundException("Клиент с ID 500 не найден!"));

        var response = mockMvc.perform(get("/api/v2/client/500"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/ClientControllerTest/client_by_id_not_found_response.json");

        Mockito.verify(clientService, Mockito.times(1)).findById(500L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateClientWithEmptyName_thenReturnError() throws Exception {
        var response = mockMvc.perform(post("/api/v2/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpsertClientRequest())))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/ClientControllerTest/empty_client_name_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    private static Stream<Arguments> invalidSizeName() {
        return Stream.of(
                Arguments.of(RandomString.make(2)),
                Arguments.of(RandomString.make(31))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidSizeName")
    public void whenCreateClientWithInvalidSizeName_thenReturnError(String name) throws Exception {
        var response = mockMvc.perform(post("/api/v2/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpsertClientRequest(name))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/ClientControllerTest/client_name_size_exception_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }



}