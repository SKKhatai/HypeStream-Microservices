package com.hypestream.inventory.consumer;

import com.hypestream.inventory.event.OrderPlacedEvent;
import com.hypestream.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final ProductRepository productRepository;

    /**
     * Kafka Listener that consumes events from the "order-placed" topic.
     * When an order is placed, this listener automatically triggers.
     */
    @KafkaListener(topics = "order-placed", groupId = "inventory-group")
    public void consumeOrderPlacedEvent(OrderPlacedEvent event) {
        log.info("Received OrderPlacedEvent from Kafka for order ID: {}", event.getOrderId());

        // Step 1: Look up the product in our MySQL database
        productRepository.findById(event.getProductId()).ifPresentOrElse(product -> {
            
            // Step 2: Check if we have enough stock
            if (product.getStock() >= event.getQuantity()) {
                
                // Step 3: Deduct the stock
                product.setStock(product.getStock() - event.getQuantity());
                productRepository.save(product);
                
                log.info("Successfully deducted {} pairs of '{}' (ID: {}). Remaining stock: {}", 
                        event.getQuantity(), product.getName(), product.getId(), product.getStock());
            } else {
                log.warn("Insufficient stock for product '{}' (ID: {}). Available: {}, Requested: {}", 
                        product.getName(), product.getId(), product.getStock(), event.getQuantity());
            }
        }, () -> log.error("Product with ID {} not found in inventory catalog!", event.getProductId()));
    }
}
