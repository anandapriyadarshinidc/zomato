package com.example.zomato.dto;

public class MenuItemResponse {

    private Long id;
    private Long restaurantId;
    private String name;
    private String description;
    private String category;
    private Double price;
    private String imageUrl;
    private Boolean availability;

    public MenuItemResponse() {
    }

    public MenuItemResponse(Long id, Long restaurantId, String name, String description,
                             String category, Double price, String imageUrl, Boolean availability) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.availability = availability;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
}
