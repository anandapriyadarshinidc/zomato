package com.example.zomato.controller;

import com.example.zomato.dto.MenuItemRequest;
import com.example.zomato.dto.MenuItemResponse;
import com.example.zomato.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminMenuController {

    private final MenuItemService menuItemService;

    public AdminMenuController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping("/admin/restaurants/{restaurantId}/menu")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse addMenuItem(@PathVariable Long restaurantId,
                                         @Valid @RequestBody MenuItemRequest request) {
        return menuItemService.addMenuItem(restaurantId, request);
    }

    @GetMapping("/admin/restaurants/{restaurantId}/menu")
    public List<MenuItemResponse> getRestaurantMenu(@PathVariable Long restaurantId) {
        return menuItemService.getMenuByRestaurant(restaurantId);
    }

    @GetMapping("/admin/menu/{menuItemId}")
    public MenuItemResponse getMenuItem(@PathVariable Long menuItemId) {
        return menuItemService.getMenuItemById(menuItemId);
    }

    @PutMapping("/admin/menu/{menuItemId}")
    public MenuItemResponse updateMenuItem(@PathVariable Long menuItemId,
                                            @Valid @RequestBody MenuItemRequest request) {
        return menuItemService.updateMenuItem(menuItemId, request);
    }

    @DeleteMapping("/admin/menu/{menuItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable Long menuItemId) {
        menuItemService.deleteMenuItem(menuItemId);
    }
}
