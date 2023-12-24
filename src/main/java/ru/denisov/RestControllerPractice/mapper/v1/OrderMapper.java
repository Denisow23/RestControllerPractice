package ru.denisov.RestControllerPractice.mapper.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.service.ClientService;
import ru.denisov.RestControllerPractice.web.model.ClientResponse;
import ru.denisov.RestControllerPractice.web.model.OrderListResponse;
import ru.denisov.RestControllerPractice.web.model.OrderResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertOrderRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ClientService clientService;

    public Order requestToOrder(UpsertOrderRequest request) {
        Order order = new Order();

        order.setCost(request.getCost());
        order.setProduct(request.getProduct());
        order.setClient(clientService.findById(request.getClientId()));

        return order;
    }

    public Order requestToOrder(Long orderId, UpsertOrderRequest request) {
        Order order = requestToOrder(request);

        order.setId(orderId);

        return order;
    }

    public OrderResponse orderToResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(order.getId());
        orderResponse.setCost(order.getCost());
        orderResponse.setProduct(order.getProduct());

        return orderResponse;
    }

    public List<OrderResponse> orderListToResponseList(List<Order> orders) {
        return orders.stream()
                .map(this::orderToResponse)
                .collect(Collectors.toList());
    }

    public OrderListResponse orderListToOrderListResponse(List<Order> orders) {
        OrderListResponse response = new OrderListResponse();
        response.setOrders(orderListToResponseList(orders));

        return response;
    }
}
