package com.example.zomato.controller;

import com.example.zomato.dto.CartItemRequest;
import com.example.zomato.dto.CartResponse;
import com.example.zomato.exception.BadRequestException;
import com.example.zomato.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse addItemToCart(@PathVariable Long userId,
                                     @Valid @RequestBody CartItemRequest request) {
        if (request.getMenuItemId() == null) {
            throw new BadRequestException("Menu ID is required");
        }
        if (request.getRestaurantId() == null) {
            throw new BadRequestException("Restaurant ID is required");
        }
        return cartService.addItemToCart(userId, request.getMenuItemId(), request.getRestaurantId(), request.getQuantity());
    }

    @GetMapping("/{userId}")
    public CartResponse getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @PutMapping("/{userId}/items")
    public CartResponse updateCartItem(@PathVariable Long userId,
                                     @Valid @RequestBody CartItemRequest request) {
        if (request.getMenuItemId() == null) {
            throw new BadRequestException("Menu ID is required");
        }
        return cartService.updateCartItem(userId, request.getMenuItemId(), request.getQuantity());
    }

    @DeleteMapping("/{userId}/items/{menuItemId}")
    public CartResponse removeCartItem(@PathVariable Long userId, @PathVariable Long menuItemId) {
        return cartService.removeCartItem(userId, menuItemId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}
