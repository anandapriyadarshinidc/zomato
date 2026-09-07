package com.example.zomato.service;

import com.example.zomato.dto.CartItemResponse;
import com.example.zomato.dto.CartResponse;
import com.example.zomato.entity.Cart;
import com.example.zomato.entity.CartItem;
import com.example.zomato.entity.MenuItem;
import com.example.zomato.entity.User;
import com.example.zomato.exception.BadRequestException;
import com.example.zomato.exception.ResourceNotFoundException;
import com.example.zomato.repository.CartItemRepository;
import com.example.zomato.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final MenuItemService menuItemService;

    public CartService(CartRepository cartRepository,
                        CartItemRepository cartItemRepository,
                        UserService userService,
                        MenuItemService menuItemService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.menuItemService = menuItemService;
    }

    public CartResponse addItemToCart(Long userId, Long menuItemId, Integer quantity) {
        return addItemToCart(userId, menuItemId, null, quantity);
    }

    public CartResponse addItemToCart(Long userId, Long menuItemId, Long restaurantId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new BadRequestException("Quantity must be at least 1");
        }

        User user = userService.findUserOrThrow(userId);
        MenuItem menuItem = menuItemService.findMenuItemOrThrow(menuItemId);

        if (restaurantId != null && !menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new BadRequestException("Menu item does not belong to restaurant: " + restaurantId);
        }
        restaurantId = menuItem.getRestaurant().getId();

        if (menuItem.getAvailability() == null || !menuItem.getAvailability()) {
            throw new BadRequestException("Menu item '" + menuItem.getName() + "' is currently unavailable");
        }

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return newCart;
        });

        // If the cart already has items from a different restaurant, block mixing restaurants.
        if (cart.getRestaurant() != null && !cart.getRestaurant().getId().equals(restaurantId)) {
            throw new BadRequestException("Cart already contains items from a different restaurant. " +
                    "Clear the cart before ordering from a new restaurant.");
        }

        if (cart.getRestaurant() == null) {
            cart.setRestaurant(menuItem.getRestaurant());
        }

        cart = cartRepository.save(cart);

        CartItem existingItem = cartItemRepository
                .findByCartIdAndMenuItemId(cart.getId(), menuItemId)
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setPrice(menuItem.getPrice());
            cartItemRepository.save(existingItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setMenuItem(menuItem);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(menuItem.getPrice());
            cart.getItems().add(cartItem);
            cartItemRepository.save(cartItem);
        }

        return getCart(userId);
    }

    public CartResponse getCart(Long userId) {
        userService.findUserOrThrow(userId);
        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart == null || cart.getItems().isEmpty()) {
            return new CartResponse(userId, cart != null && cart.getRestaurant() != null ? cart.getRestaurant().getId() : null,
                    Collections.emptyList(), 0.0);
        }

        return toResponse(cart);
    }

    public CartResponse updateCartItem(Long userId, Long menuItemId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new BadRequestException("Quantity must be at least 1");
        }

        Cart cart = findCartOrThrow(userId);
        CartItem cartItem = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found in cart: " + menuItemId));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return getCart(userId);
    }

    public CartResponse removeCartItem(Long userId, Long menuItemId) {
        Cart cart = findCartOrThrow(userId);
        CartItem cartItem = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found in cart: " + menuItemId));

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        if (cart.getItems().isEmpty()) {
            cart.setRestaurant(null);
            cartRepository.save(cart);
        }

        return getCart(userId);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null) {
            return;
        }
        cart.getItems().clear();
        cart.setRestaurant(null);
        cartRepository.save(cart);
    }

    /**
     * Fetches the cart entity for internal use (e.g. by OrderService), throwing if not found or empty.
     */
    public Cart getCartEntityOrThrow(Long userId) {
        return findCartOrThrow(userId);
    }

    private Cart findCartOrThrow(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getMenuItem().getId(),
                        item.getMenuItem().getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getTotal()
                ))
                .collect(Collectors.toList());

        double totalAmount = itemResponses.stream()
                .mapToDouble(CartItemResponse::getTotal)
                .sum();

        return new CartResponse(
                cart.getUser().getId(),
                cart.getRestaurant() != null ? cart.getRestaurant().getId() : null,
                itemResponses,
                totalAmount
        );
    }
}
