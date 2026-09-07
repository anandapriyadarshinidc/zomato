package com.example.zomato.service;

import com.example.zomato.dto.MenuItemRequest;
import com.example.zomato.dto.MenuItemResponse;
import com.example.zomato.entity.MenuItem;
import com.example.zomato.entity.Restaurant;
import com.example.zomato.exception.ResourceNotFoundException;
import com.example.zomato.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantService restaurantService;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantService restaurantService) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantService = restaurantService;
    }

    public MenuItemResponse addMenuItem(Long restaurantId, MenuItemRequest request) {
        Restaurant restaurant = restaurantService.findRestaurantOrThrow(restaurantId);

        MenuItem menuItem = new MenuItem();
        menuItem.setRestaurant(restaurant);
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setCategory(request.getCategory());
        menuItem.setPrice(request.getPrice());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setAvailability(request.getAvailability());

        MenuItem saved = menuItemRepository.save(menuItem);
        return toResponse(saved);
    }

    public List<MenuItemResponse> getMenuByRestaurant(Long restaurantId) {
        restaurantService.findRestaurantOrThrow(restaurantId);
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MenuItemResponse getMenuItemById(Long menuItemId) {
        MenuItem menuItem = findMenuItemOrThrow(menuItemId);
        return toResponse(menuItem);
    }

    public MenuItemResponse updateMenuItem(Long menuItemId, MenuItemRequest request) {
        MenuItem menuItem = findMenuItemOrThrow(menuItemId);

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setCategory(request.getCategory());
        menuItem.setPrice(request.getPrice());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setAvailability(request.getAvailability());

        MenuItem updated = menuItemRepository.save(menuItem);
        return toResponse(updated);
    }

    public void deleteMenuItem(Long menuItemId) {
        MenuItem menuItem = findMenuItemOrThrow(menuItemId);
        menuItemRepository.delete(menuItem);
    }

    public MenuItem findMenuItemOrThrow(Long menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuItemId));
    }

    private MenuItemResponse toResponse(MenuItem menuItem) {
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getRestaurant() != null ? menuItem.getRestaurant().getId() : null,
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getCategory(),
                menuItem.getPrice(),
                menuItem.getImageUrl(),
                menuItem.getAvailability()
        );
    }
}
