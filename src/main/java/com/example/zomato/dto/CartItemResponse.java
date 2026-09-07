package com.example.zomato.dto;

public class CartItemResponse {

    private Long menuItemId;
    private String name;
    private Double price;
    private Integer quantity;
    private Double total;

    public CartItemResponse() {
    }

    public CartItemResponse(Long menuItemId, String name, Double price, Integer quantity, Double total) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
