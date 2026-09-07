package com.example.zomato.dto;

import java.util.List;

public class CartResponse {

    private Long userId;
    private Long restaurantId;
    private List<CartItemResponse> items;
    private Double totalAmount;

    public CartResponse() {
    }

    public CartResponse(Long userId, Long restaurantId, List<CartItemResponse> items, Double totalAmount) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.items = items;
        this.totalAmount = totalAmount;
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

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
