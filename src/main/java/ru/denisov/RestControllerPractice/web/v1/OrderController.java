package ru.denisov.RestControllerPractice.web.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.denisov.RestControllerPractice.mapper.v1.OrderMapper;
import ru.denisov.RestControllerPractice.model.Order;
import ru.denisov.RestControllerPractice.service.OrderService;
import ru.denisov.RestControllerPractice.web.model.OrderListResponse;
import ru.denisov.RestControllerPractice.web.model.OrderResponse;
import ru.denisov.RestControllerPractice.web.model.UpsertOrderRequest;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<OrderListResponse> findAll() {
        return ResponseEntity.ok(orderMapper.orderListToOrderListResponse(orderService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                orderMapper.orderToResponse(orderService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody UpsertOrderRequest request) {
        Order newOrder = orderService.save(orderMapper.requestToOrder(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.orderToResponse(newOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable("id") Long orderId
                                        , @RequestBody UpsertOrderRequest request) {
        Order updatedOrder = orderService.update(orderMapper.requestToOrder(orderId, request));

        return ResponseEntity.ok(orderMapper.orderToResponse(updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
