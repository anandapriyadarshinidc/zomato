package com.example.zomato.controller;

import com.example.zomato.dto.OrderRequest;
import com.example.zomato.dto.OrderResponse;
import com.example.zomato.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@PathVariable Long userId, @Valid @RequestBody OrderRequest request) {
        return orderService.placeOrder(userId, request);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

    @PutMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}
