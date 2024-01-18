package ru.denisov.RestControllerPractice.web.mapper.v2;

import org.springframework.beans.factory.annotation.Autowired;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.service.ClientService;
import ru.denisov.RestControllerPractice.web.model.UpsertOrderRequest;

public abstract class OrderMapperDelegate implements OrderMapperV2 {

    @Autowired
    private ClientService databaseClientService;

    @Override
    public Order requestToOrder(UpsertOrderRequest request) {
        Order order = new Order();
        order.setCost(request.getCost());
        order.setProduct(request.getProduct());
        order.setClient(databaseClientService.findById(request.getClientId()));

        return order;
    }

    @Override
    public Order requestToOrder(Long orderId, UpsertOrderRequest request) {
        Order order = requestToOrder(request);
        order.setId(orderId);

        return order;
    }
}
