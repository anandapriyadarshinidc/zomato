package com.example.zomato.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartItemRequest {

    private Long restaurantId;

    @JsonAlias({"menuId", "menuItemId"})
    private Long menuItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getMenuId() {
        return menuItemId;
    }

    public void setMenuId(Long menuId) {
        this.menuItemId = menuId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
