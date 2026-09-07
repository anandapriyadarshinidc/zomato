package com.example.zomato.service;

import com.example.zomato.dto.OrderItemResponse;
import com.example.zomato.dto.OrderRequest;
import com.example.zomato.dto.OrderResponse;
import com.example.zomato.entity.*;
import com.example.zomato.exception.BadRequestException;
import com.example.zomato.exception.ResourceNotFoundException;
import com.example.zomato.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, UserService userService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.cartService = cartService;
    }

    public OrderResponse placeOrder(Long userId, OrderRequest request) {
        User user = userService.findUserOrThrow(userId);
        Cart cart = cartService.getCartEntityOrThrow(userId);

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cannot place order: cart is empty");
        }

        for (CartItem cartItem : cart.getItems()) {
            MenuItem menuItem = cartItem.getMenuItem();
            if (menuItem.getAvailability() == null || !menuItem.getAvailability()) {
                throw new BadRequestException("Menu item '" + menuItem.getName() + "' is no longer available");
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(cart.getRestaurant());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setStatus(OrderStatus.PLACED);
        order.setOrderTime(LocalDateTime.now());

        double total = 0.0;
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(cartItem.getMenuItem());
            orderItem.setName(cartItem.getMenuItem().getName());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotal(cartItem.getTotal());
            order.getOrderItems().add(orderItem);
            total += orderItem.getTotal();
        }
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        cartService.clearCart(userId);

        return toResponse(saved);
    }

    public OrderResponse getOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);
        return toResponse(order);
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        userService.findUserOrThrow(userId);
        return orderRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getRestaurantOrders(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse cancelOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);

        if (order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.CANCELLED
                || order.getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
            throw new BadRequestException("Order cannot be cancelled in its current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order updated = orderRepository.save(order);
        return toResponse(updated);
    }

    public OrderResponse confirmOrder(Long orderId) {
        return transitionStatus(orderId, OrderStatus.PLACED, OrderStatus.CONFIRMED);
    }

    public OrderResponse prepareOrder(Long orderId) {
        return transitionStatus(orderId, OrderStatus.CONFIRMED, OrderStatus.PREPARING);
    }

    public OrderResponse markReady(Long orderId) {
        return transitionStatus(orderId, OrderStatus.PREPARING, OrderStatus.READY);
    }

    public OrderResponse markOutForDelivery(Long orderId) {
        return transitionStatus(orderId, OrderStatus.READY, OrderStatus.OUT_FOR_DELIVERY);
    }

    public OrderResponse markDelivered(Long orderId) {
        return transitionStatus(orderId, OrderStatus.OUT_FOR_DELIVERY, OrderStatus.DELIVERED);
    }

    private OrderResponse transitionStatus(Long orderId, OrderStatus expectedCurrent, OrderStatus newStatus) {
        Order order = findOrderOrThrow(orderId);

        if (order.getStatus() != expectedCurrent) {
            throw new BadRequestException("Invalid order status transition: order is currently "
                    + order.getStatus() + ", expected " + expectedCurrent + " to move to " + newStatus);
        }

        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);
        return toResponse(updated);
    }

    private Order findOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getMenuItem().getId(),
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getTotal()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getRestaurant() != null ? order.getRestaurant().getId() : null,
                order.getDeliveryAddress(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getOrderTime(),
                itemResponses
        );
    }
}
