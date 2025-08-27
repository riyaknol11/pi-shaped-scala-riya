package com.demo.ecommerce.controller;

import com.demo.ecommerce.model.OrderEvent;
import com.demo.ecommerce.producer.OrderProducer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderProducer orderProducer;

    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
        try {
            String orderId = UUID.randomUUID().toString();

            OrderEvent orderEvent = OrderEvent.builder()
                    .orderId(orderId)
                    .customerId(request.getCustomerId())
                    .customerEmail(request.getCustomerEmail())
                    .items(request.getItems())
                    .totalAmount(calculateTotal(request.getItems()))
                    .orderTime(LocalDateTime.now())
                    .status("PLACED")
                    .build();

            orderProducer.publishOrderEvent(orderEvent);

            return ResponseEntity.ok("Order placed successfully. Order ID: " + orderId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to place order: " + e.getMessage());
        }
    }

    @GetMapping("/demo")
    public ResponseEntity<String> placeDemoOrder() {
        try {
            String orderId = UUID.randomUUID().toString();

            OrderEvent demoOrder = OrderEvent.builder()
                    .orderId(orderId)
                    .customerId("CUSTOMER_123")
                    .customerEmail("customer@example.com")
                    .items(Arrays.asList(
                            OrderEvent.OrderItem.builder()
                                    .productId("PROD_001")
                                    .productName("Laptop")
                                    .quantity(1)
                                    .price(999.99)
                                    .build(),
                            OrderEvent.OrderItem.builder()
                                    .productId("PROD_002")
                                    .productName("Mouse")
                                    .quantity(2)
                                    .price(29.99)
                                    .build()
                    ))
                    .totalAmount(1059.97)
                    .orderTime(LocalDateTime.now())
                    .status("PLACED")
                    .build();

            orderProducer.publishOrderEvent(demoOrder);

            return ResponseEntity.ok("Demo order placed successfully. Order ID: " + orderId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to place demo order: " + e.getMessage());
        }
    }

    private double calculateTotal(java.util.List<OrderEvent.OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    @Data
    public static class OrderRequest {
        private String customerId;
        private String customerEmail;
        private java.util.List<OrderEvent.OrderItem> items;
    }
}