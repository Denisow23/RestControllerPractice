package ru.denisov.RestControllerPractice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.web.model.ClientResponse;
import ru.denisov.RestControllerPractice.web.model.OrderResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractTestController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected Client createClient(Long id, Order order) {
        Client client = new Client(
                id,
                "Client" + id,
                new ArrayList<>()
                );
        if (order != null) {
            order.setClient(client);
            client.addOrder(order);
        }

        return client;
    }

    protected Order createOrder(Long id, Long cost, Client client) {
        return new Order(id, "Test product " + id,
                BigDecimal.valueOf(cost), client,
//        null, null);
                Instant.now(), Instant.now());
    }

    protected ClientResponse createClientResponse(Long id, OrderResponse orderResponse) {
        ClientResponse clientResponse = new ClientResponse(
                id,
                "Client " + id,
                new ArrayList<>()
        );

        if(orderResponse != null) {
            clientResponse.getOrders().add(orderResponse);
        }

        return clientResponse;
    }

    protected OrderResponse createOrderResponse(Long id, Long cost) {
        return new OrderResponse(id, "Test product " + id, BigDecimal.valueOf(cost));
    }

}
