package com.example.zomato.service;

import com.example.zomato.dto.RestaurantRequest;
import com.example.zomato.dto.RestaurantResponse;
import com.example.zomato.entity.Restaurant;
import com.example.zomato.entity.RestaurantStatus;
import com.example.zomato.exception.BadRequestException;
import com.example.zomato.exception.ResourceNotFoundException;
import com.example.zomato.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setRating(request.getRating());
        restaurant.setStatus(parseStatus(request.getStatus()));

        Restaurant saved = restaurantRepository.save(restaurant);
        return toResponse(saved);
    }

    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RestaurantResponse getRestaurantById(Long restaurantId) {
        Restaurant restaurant = findRestaurantOrThrow(restaurantId);
        return toResponse(restaurant);
    }

    public RestaurantResponse updateRestaurant(Long restaurantId, RestaurantRequest request) {
        Restaurant restaurant = findRestaurantOrThrow(restaurantId);

        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setRating(request.getRating());
        restaurant.setStatus(parseStatus(request.getStatus()));

        Restaurant updated = restaurantRepository.save(restaurant);
        return toResponse(updated);
    }

    public void deleteRestaurant(Long restaurantId) {
        Restaurant restaurant = findRestaurantOrThrow(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    public Restaurant findRestaurantOrThrow(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
    }

    private RestaurantStatus parseStatus(String status) {
        try {
            return RestaurantStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid restaurant status: " + status + ". Allowed values: OPEN, CLOSED");
        }
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getAddress(),
                restaurant.getCuisine(),
                restaurant.getRating(),
                restaurant.getStatus() != null ? restaurant.getStatus().name() : null
        );
    }
}
