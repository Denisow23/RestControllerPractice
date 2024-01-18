package ru.denisov.RestControllerPractice.web.controller.v2;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.service.OrderService;
import ru.denisov.RestControllerPractice.web.mapper.v2.OrderMapperV2;
import ru.denisov.RestControllerPractice.web.model.OrderFilter;
import ru.denisov.RestControllerPractice.web.model.OrderListResponse;
import ru.denisov.RestControllerPractice.web.model.OrderResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertOrderRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v2/order")
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderService databaseOrderService;

    private final OrderMapperV2 orderMapper;

    @GetMapping("/filter")
    public ResponseEntity<OrderListResponse> filterBy(@Valid OrderFilter orderFilter) {
        return ResponseEntity.ok(
                orderMapper.orderListToOrderListResponse(
                        databaseOrderService.filterBy(orderFilter)
                )
        );
    }

    @GetMapping
    public ResponseEntity<OrderListResponse> findAll() {
        return ResponseEntity.ok(
                orderMapper.orderListToOrderListResponse(
                        databaseOrderService.findAll()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                orderMapper.orderToResponse(
                        databaseOrderService.findById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid UpsertOrderRequest request) {
        Order newOrder = databaseOrderService.save(
                orderMapper.requestToOrder(request)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderMapper.orderToResponse(newOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable("id") Long orderId
            , @RequestBody @Valid UpsertOrderRequest request) {
        Order updatedOrder = databaseOrderService.update(orderMapper.requestToOrder(orderId, request));

        return ResponseEntity.ok(orderMapper.orderToResponse(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        databaseOrderService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
