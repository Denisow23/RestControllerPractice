package ru.denisov.RestControllerPractice.web.controller.v1;

import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.denisov.RestControllerPractice.AbstractTestController;
import ru.denisov.RestControllerPractice.StringTestUtils;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.service.OrderService;
import ru.denisov.RestControllerPractice.web.mapper.v1.OrderMapper;
import ru.denisov.RestControllerPractice.web.model.ClientResponse;
import ru.denisov.RestControllerPractice.web.model.OrderListResponse;
import ru.denisov.RestControllerPractice.web.model.OrderResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertOrderRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends AbstractTestController {

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    @Test
    public void whenFindAll_thenReturnOrders() throws Exception {
        List<Order> orderList = new ArrayList<>();
        Client client1 = createClient(1L, null);
        orderList.add(createOrder(1L, 500L, client1));
        Client client2 = createClient(2L, null);
        orderList.add(createOrder(2L, 1000L, client2));

        List<OrderResponse> orderResponses = new ArrayList<>();
        orderResponses.add(createOrderResponse(1L, 500L));
        orderResponses.add(createOrderResponse(2L, 1000L));

        OrderListResponse orderListResponse = new OrderListResponse(orderResponses);

        Mockito.when(orderService.findAll()).thenReturn(orderList);
        Mockito.when(orderMapper.orderListToOrderListResponse(orderList)).thenReturn(orderListResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/order"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/OrderControllerTest/find_all_orders_response.json");

        Mockito.verify(orderService, Mockito.times(1)).findAll();
        Mockito.verify(orderMapper, Mockito.times(1)).orderListToOrderListResponse(orderList);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenGetOrderById_thenReturnOrderById() throws Exception {
        Order order = createOrder(1L, 500L, null);

        OrderResponse orderResponse = createOrderResponse(1L, 500L);

        Mockito.when(orderService.findById(1L)).thenReturn(order);
        Mockito.when(orderMapper.orderToResponse(order)).thenReturn(orderResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/order/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/OrderControllerTest/find_order_by_id_response.json");

        Mockito.verify(orderService, Mockito.times(1)).findById(1L);
        Mockito.verify(orderMapper, Mockito.times(1)).orderToResponse(order);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    }

    @Test
    public void whenCreateOrder_thenReturnNewOrder() throws Exception {
        Order order = new Order();
        order.setCost(BigDecimal.valueOf(500L));
        order.setProduct("Test product 1");
        Client client = createClient(1L, null);
        order.setClient(client);
        Order createdOrder = createOrder(1L, 500L, client);
        OrderResponse orderResponse = createOrderResponse(1L, 500L);
        UpsertOrderRequest request = new UpsertOrderRequest(1L, "Test product 1", BigDecimal.valueOf(500L));

        Mockito.when(orderMapper.requestToOrder(request)).thenReturn(order);
        Mockito.when(orderService.save(order)).thenReturn(createdOrder);
        Mockito.when(orderMapper.orderToResponse(createdOrder)).thenReturn(orderResponse);

        String actualResponse = mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/OrderControllerTest/create_order_response.json");

        Mockito.verify(orderMapper, Mockito.times(1)).requestToOrder(request);
        Mockito.verify(orderService, Mockito.times(1)).save(order);
        Mockito.verify(orderMapper, Mockito.times(1)).orderToResponse(createdOrder);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}
