package ru.denisov.RestControllerPractice.web.mapper.v2;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.service.OrderService;
import ru.denisov.RestControllerPractice.web.model.OrderResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertOrderRequest;
import ru.denisov.RestControllerPractice.web.model.OrderListResponse;

import java.util.List;

@DecoratedWith(OrderMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapperV2 {

    Order requestToOrder(UpsertOrderRequest request);

    @Mapping(source = "orderId", target = "id")
    Order requestToOrder(Long orderId, UpsertOrderRequest request);

    OrderResponse orderToResponse(Order order);

    List<OrderResponse> orderListToResponseList(List<Order> orders);

    default OrderListResponse orderListToOrderListResponse(List<Order> orders){
        OrderListResponse response = new OrderListResponse();
        response.setOrders(orderListToResponseList(orders));

        return response;
    }



}
