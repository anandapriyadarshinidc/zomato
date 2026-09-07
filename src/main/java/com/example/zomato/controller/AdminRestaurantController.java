package com.example.zomato.controller;

import com.example.zomato.dto.RestaurantRequest;
import com.example.zomato.dto.RestaurantResponse;
import com.example.zomato.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/restaurants")
public class AdminRestaurantController {

    private final RestaurantService restaurantService;

    public AdminRestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse createRestaurant(@Valid @RequestBody RestaurantRequest request) {
        return restaurantService.createRestaurant(request);
    }

    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{restaurantId}")
    public RestaurantResponse getRestaurantById(@PathVariable Long restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }

    @PutMapping("/{restaurantId}")
    public RestaurantResponse updateRestaurant(@PathVariable Long restaurantId,
                                                @Valid @RequestBody RestaurantRequest request) {
        return restaurantService.updateRestaurant(restaurantId, request);
    }

    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable Long restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
    }
}
