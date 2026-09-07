package com.example.zomato.controller;

import com.example.zomato.dto.OrderResponse;
import com.example.zomato.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/restaurants/{restaurantId}/orders")
    public List<OrderResponse> getRestaurantOrders(@PathVariable Long restaurantId) {
        return orderService.getRestaurantOrders(restaurantId);
    }

    @PutMapping("/admin/orders/{orderId}/confirm")
    public OrderResponse confirmOrder(@PathVariable Long orderId) {
        return orderService.confirmOrder(orderId);
    }

    @PutMapping("/admin/orders/{orderId}/prepare")
    public OrderResponse prepareOrder(@PathVariable Long orderId) {
        return orderService.prepareOrder(orderId);
    }

    @PutMapping("/admin/orders/{orderId}/ready")
    public OrderResponse markReady(@PathVariable Long orderId) {
        return orderService.markReady(orderId);
    }

    @PutMapping("/admin/orders/{orderId}/out-for-delivery")
    public OrderResponse markOutForDelivery(@PathVariable Long orderId) {
        return orderService.markOutForDelivery(orderId);
    }

    @PutMapping("/admin/orders/{orderId}/delivered")
    public OrderResponse markDelivered(@PathVariable Long orderId) {
        return orderService.markDelivered(orderId);
    }
}
