package ru.denisov.RestControllerPractice.web.controller.v1;

import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import ru.denisov.RestControllerPractice.AbstractTestController;
import ru.denisov.RestControllerPractice.StringTestUtils;
import ru.denisov.RestControllerPractice.model.Client;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.repository.exception.EntityNotFoundException;
import ru.denisov.RestControllerPractice.service.OrderService;
import ru.denisov.RestControllerPractice.web.mapper.v1.OrderMapper;
import ru.denisov.RestControllerPractice.web.model.OrderListResponse;
import ru.denisov.RestControllerPractice.web.model.OrderResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertClientRequest;
import ru.denisov.RestControllerPractice.web.model.UpsertOrderRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

    @Test
    public void whenUpdateOrder_thenReturnUpdatedOrder() throws Exception {
        UpsertOrderRequest request = new UpsertOrderRequest(1L, "New Updated Product 1", BigDecimal.valueOf(5000L));
        Order updatedOrder = Order.builder()
                .id(1L)
                .product("New updated product 1")
                .client(createClient(1L, null))
                .updateAt(Instant.now())
                .createAt(Instant.now())
                .cost(BigDecimal.valueOf(5000L))
                .build();

        OrderResponse response = new OrderResponse(1L, "New Updated Product 1", BigDecimal.valueOf(5000L));

        Mockito.when(orderMapper.requestToOrder(1L, request)).thenReturn(updatedOrder);
        Mockito.when(orderService.update(updatedOrder)).thenReturn(updatedOrder);
        Mockito.when(orderMapper.orderToResponse(updatedOrder)).thenReturn(response);

        String actualResponse = mockMvc.perform(put("/api/v1/order/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/OrderControllerTest/update_order_response.json");

        Mockito.verify(orderMapper, Mockito.times(1)).requestToOrder(1L, request);
        Mockito.verify(orderService, Mockito.times(1)).update(updatedOrder);
        Mockito.verify(orderMapper, Mockito.times(1)).orderToResponse(updatedOrder);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteOrder_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/order/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(orderService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void whenFindByIdNotExistsOrder_thenReturnError() throws Exception {
        Mockito.when(orderService.findById(500L)).thenThrow(
                new EntityNotFoundException("Заказ с ID 500 не найден!")
        );

        var response = mockMvc.perform(get("/api/v1/order/500"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/OrderControllerTest/find_order_by_id_not_exists.json");

        Mockito.verify(orderService, Mockito.times(1)).findById(500L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }


    @Test
    public void whenInvalidId_thenReturnError() throws Exception{
        var response = mockMvc.perform(post("/api/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpsertOrderRequest(null, "Product", BigDecimal.valueOf(500L)))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/OrderControllerTest/empty_id_client_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }


}
