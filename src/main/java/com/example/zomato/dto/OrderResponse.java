package com.example.zomato.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private Long userId;
    private Long restaurantId;
    private String deliveryAddress;
    private String status;
    private Double totalAmount;
    private LocalDateTime orderTime;
    private List<OrderItemResponse> items;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long userId, Long restaurantId, String deliveryAddress,
                          String status, Double totalAmount, LocalDateTime orderTime,
                          List<OrderItemResponse> items) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderTime = orderTime;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}
