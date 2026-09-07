# Zomato-Like Food Ordering Backend

A simple monolithic Spring Boot backend for a Zomato-style food ordering system, with **Admin** and **User** sides. No authentication, no payment gateway, no microservices — just the core restaurant → menu → cart → order flow, backed by MySQL.

## Tech Stack

- Java 17
- Spring Boot 3.2.5
- Spring Web (REST APIs)
- Spring Data JPA + Hibernate
- MySQL
- Maven

## Project Structure

```text
zomato-backend/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/example/zomato/
    │   ├── ZomatoBackendApplication.java
    │   ├── config/
    │   │   └── DataSeeder.java          # seeds the one static user on startup
    │   ├── controller/
    │   │   ├── AdminRestaurantController.java
    │   │   ├── AdminMenuController.java
    │   │   ├── AdminOrderController.java
    │   │   ├── UserRestaurantController.java
    │   │   ├── CartController.java
    │   │   └── OrderController.java
    │   ├── service/
    │   │   ├── RestaurantService.java
    │   │   ├── MenuItemService.java
    │   │   ├── UserService.java
    │   │   ├── CartService.java
    │   │   └── OrderService.java
    │   ├── repository/
    │   │   ├── RestaurantRepository.java
    │   │   ├── MenuItemRepository.java
    │   │   ├── UserRepository.java
    │   │   ├── CartRepository.java
    │   │   ├── CartItemRepository.java
    │   │   ├── OrderRepository.java
    │   │   └── OrderItemRepository.java
    │   ├── entity/
    │   │   ├── Restaurant.java
    │   │   ├── MenuItem.java
    │   │   ├── User.java
    │   │   ├── Cart.java
    │   │   ├── CartItem.java
    │   │   ├── Order.java
    │   │   ├── OrderItem.java
    │   │   ├── RestaurantStatus.java
    │   │   └── OrderStatus.java
    │   ├── dto/
    │   │   ├── RestaurantRequest.java / RestaurantResponse.java
    │   │   ├── MenuItemRequest.java / MenuItemResponse.java
    │   │   ├── CartItemRequest.java / CartItemResponse.java / CartResponse.java
    │   │   ├── OrderRequest.java / OrderResponse.java / OrderItemResponse.java
    │   │   └── ErrorResponse.java
    │   └── exception/
    │       ├── ResourceNotFoundException.java
    │       ├── BadRequestException.java
    │       └── GlobalExceptionHandler.java
    └── resources/
        └── application.properties
```

Layered architecture: `Controller → Service → Repository → JPA/Hibernate → MySQL`.

## No Login / Authentication

There is no user registration or login. A single **static user** (`id = 1`, name `Static User`) is automatically inserted into the `users` table the first time the app starts (see `DataSeeder`). All cart/order endpoints that take a `{userId}` should use `1`.

## How to Run

### 1. Prerequisites

- Java 17+
- Maven 3.6+
- MySQL running locally on `localhost:3306`, with a user `root` / password `root` (or edit `application.properties` to match your credentials)

### 2. Configure (if needed)

Edit `src/main/resources/application.properties` if your MySQL username/password differ from `root` / `root`. The database `zomato_db` is created automatically (`createDatabaseIfNotExist=true`) and all tables are created/updated automatically by Hibernate (`spring.jpa.hibernate.ddl-auto=update`) — no manual SQL needed.

### 3. Build and run

```bash
cd zomato-backend
mvn clean install
mvn spring-boot:run
```

The app starts on:

```text
http://localhost:9091
```

On first startup, Hibernate creates the schema and `DataSeeder` inserts the static user (`id = 1`).

## API Reference

### Admin — Restaurants

| Method | Path | Body |
|---|---|---|
| POST | `/admin/restaurants` | `RestaurantRequest` |
| GET | `/admin/restaurants` | — |
| GET | `/admin/restaurants/{restaurantId}` | — |
| PUT | `/admin/restaurants/{restaurantId}` | `RestaurantRequest` |
| DELETE | `/admin/restaurants/{restaurantId}` | — |

**Create restaurant — request**
```json
{
  "name": "Pizza Palace",
  "description": "Italian and fast food restaurant",
  "address": "MG Road",
  "cuisine": "Italian",
  "rating": 4.5,
  "status": "OPEN"
}
```

**Response**
```json
{
  "id": 1,
  "name": "Pizza Palace",
  "description": "Italian and fast food restaurant",
  "address": "MG Road",
  "cuisine": "Italian",
  "rating": 4.5,
  "status": "OPEN"
}
```

`status` accepts `OPEN` or `CLOSED`.

### Admin — Menu

| Method | Path | Body |
|---|---|---|
| POST | `/admin/restaurants/{restaurantId}/menu` | `MenuItemRequest` |
| GET | `/admin/restaurants/{restaurantId}/menu` | — |
| GET | `/admin/menu/{menuItemId}` | — |
| PUT | `/admin/menu/{menuItemId}` | `MenuItemRequest` |
| DELETE | `/admin/menu/{menuItemId}` | — |

**Add menu item — request**
```json
{
  "name": "Margherita Pizza",
  "description": "Classic cheese pizza",
  "category": "Pizza",
  "price": 299,
  "imageUrl": "pizza.jpg",
  "availability": true
}
```

**Response**
```json
{
  "id": 1,
  "restaurantId": 1,
  "name": "Margherita Pizza",
  "description": "Classic cheese pizza",
  "category": "Pizza",
  "price": 299.0,
  "imageUrl": "pizza.jpg",
  "availability": true
}
```

### User — Browse

| Method | Path |
|---|---|
| GET | `/restaurants` |
| GET | `/restaurants/{restaurantId}` |
| GET | `/restaurants/{restaurantId}/menu` |

### Cart

| Method | Path | Body |
|---|---|---|
| POST | `/cart/{userId}/items` | `{ "restaurantId": 1, "menuId": 1, "quantity": 2 }` |
| GET | `/cart/{userId}` | — |
| PUT | `/cart/{userId}/items` | `{ "restaurantId": 1, "menuId": 1, "quantity": 3 }` |
| DELETE | `/cart/{userId}/items/{menuItemId}` | — |
| DELETE | `/cart/{userId}` | — |

**View cart — response**
```json
{
  "userId": 1,
  "restaurantId": 1,
  "items": [
    {
      "menuItemId": 1,
      "name": "Margherita Pizza",
      "price": 299.0,
      "quantity": 2,
      "total": 598.0
    }
  ],
  "totalAmount": 598.0
}
```

> Business rule: a cart can only hold items from one restaurant at a time. Adding an item from a different restaurant while the cart already has items is rejected — clear the cart first.

### Orders (User)

| Method | Path | Body |
|---|---|---|
| POST | `/orders/{userId}` | `{ "deliveryAddress": "Indiranagar, Bangalore" }` |
| GET | `/orders/{orderId}` | — |
| GET | `/orders/user/{userId}` | — |
| PUT | `/orders/{orderId}/cancel` | — |

**Place order — response**
```json
{
  "id": 10,
  "userId": 1,
  "restaurantId": 1,
  "deliveryAddress": "Indiranagar, Bangalore",
  "status": "PLACED",
  "totalAmount": 598.0,
  "orderTime": "2026-09-04T10:15:30",
  "items": [
    {
      "menuItemId": 1,
      "name": "Margherita Pizza",
      "price": 299.0,
      "quantity": 2,
      "total": 598.0
    }
  ]
}
```

Placing an order: validates the cart isn't empty, validates every item is still available, creates the order + order items, computes the total, saves it, clears the cart, and returns the order.

Order statuses: `PLACED → CONFIRMED → PREPARING → READY → OUT_FOR_DELIVERY → DELIVERED`, or `CANCELLED` (only allowed while the order hasn't reached `OUT_FOR_DELIVERY`/`DELIVERED`/`CANCELLED`).

### Orders (Admin)

| Method | Path |
|---|---|
| GET | `/admin/restaurants/{restaurantId}/orders` |
| PUT | `/admin/orders/{orderId}/confirm` |
| PUT | `/admin/orders/{orderId}/prepare` |
| PUT | `/admin/orders/{orderId}/ready` |
| PUT | `/admin/orders/{orderId}/out-for-delivery` |
| PUT | `/admin/orders/{orderId}/delivered` |

Each status-update endpoint only succeeds if the order is currently in the expected preceding status (e.g. `confirm` only works on a `PLACED` order); otherwise it returns `400 Bad Request`.

## Error Responses

All errors are handled by a single `@RestControllerAdvice` (`GlobalExceptionHandler`) and return a consistent JSON shape:

```json
{
  "timestamp": "2026-09-04T10:15:30",
  "status": 404,
  "error": "Not Found",
  "message": "Restaurant not found with id: 99"
}
```

| Situation | HTTP Status |
|---|---|
| Restaurant / menu item / cart / order not found | 404 |
| Empty cart on order placement | 400 |
| Invalid quantity | 400 |
| Menu item unavailable | 400 |
| Invalid order status transition | 400 |
| Request body validation failure (e.g. missing name) | 400 |
| Uncaught error | 500 |

## Notes

- No `@RequestParam` is used anywhere — all identifiers are path variables, and all payloads are JSON request bodies (`@RequestBody`), per the API design rules.
- No login/auth, payment gateway, reviews, or notifications are implemented — this is intentionally scoped to the core browse → cart → order → track flow.
