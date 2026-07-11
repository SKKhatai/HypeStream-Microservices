package com.hypestream.order.controller;

import com.hypestream.order.model.Order;
import com.hypestream.order.model.OrderStatus;
import com.hypestream.order.event.OrderPlacedEvent;
import com.hypestream.order.producer.OrderProducer;
import com.hypestream.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    /**
     * GET endpoint to view all orders placed in the system.
     * Path: GET /api/v1/orders
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * POST endpoint to place a new order.
     * Path: POST /api/v1/orders
     * 
     * In this request, the client only sends:
     * - productId
     * - quantity
     */
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest) {
        BigDecimal totalprice = BigDecimal.valueOf(orderRequest.getQuantity()).multiply(new BigDecimal("150.00"));

        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .totalPrice(totalprice)
                .status(OrderStatus.PENDING)
                .build();
        Order savedOrder = orderRepository.save(order);

        // Publish OrderPlacedEvent to Kafka topic
        orderProducer.sendOrderPlacedEvent(OrderPlacedEvent.builder()
                .orderId(savedOrder.getId())
                .productId(savedOrder.getProductId())
                .quantity(savedOrder.getQuantity())
                .build());

        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    /**
     * A simple DTO (Data Transfer Object) class to read incoming JSON requests.
     * DTOs prevent users from sending database fields (like order status or ID) 
     * directly in the request payload.
     */
    @lombok.Data
    public static class OrderRequest {
        private Long productId;
        private Integer quantity;
    }
}
