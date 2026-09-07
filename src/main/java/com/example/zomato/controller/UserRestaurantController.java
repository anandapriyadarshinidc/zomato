package com.example.zomato.controller;

import com.example.zomato.dto.MenuItemResponse;
import com.example.zomato.dto.RestaurantResponse;
import com.example.zomato.service.MenuItemService;
import com.example.zomato.service.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class UserRestaurantController {

    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;

    public UserRestaurantController(RestaurantService restaurantService, MenuItemService menuItemService) {
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{restaurantId}")
    public RestaurantResponse getRestaurantById(@PathVariable Long restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }

    @GetMapping("/{restaurantId}/menu")
    public List<MenuItemResponse> getRestaurantMenu(@PathVariable Long restaurantId) {
        return menuItemService.getMenuByRestaurant(restaurantId);
    }
}
