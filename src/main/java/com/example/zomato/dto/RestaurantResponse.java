package com.example.zomato.dto;

public class RestaurantResponse {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String cuisine;
    private Double rating;
    private String status;

    public RestaurantResponse() {
    }

    public RestaurantResponse(Long id, String name, String description, String address,
                               String cuisine, Double rating, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.cuisine = cuisine;
        this.rating = rating;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
