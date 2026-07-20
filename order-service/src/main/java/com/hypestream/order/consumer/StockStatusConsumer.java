package com.hypestream.order.consumer;

import com.hypestream.order.event.InventoryReservedEvent;
import com.hypestream.order.model.Order;
import com.hypestream.order.model.OrderStatus;
import com.hypestream.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockStatusConsumer {

    private final OrderRepository orderRepository;

    /**
     * Kafka Listener that consumes events from the "order-stock-status" topic.
     * When inventory-service replies with the stock status, this listener triggers.
     * Retrieves the order from the database and updates its status to CONFIRMED 
     * or CANCELLED based on the stock reservation result from Kafka.
     */
    @KafkaListener(topics = "order-stock-status", groupId = "order-group")
    public void consumeStockStatusEvent(InventoryReservedEvent event) {
        log.info("Received InventoryReservedEvent from Kafka for order ID: {} with status: {}", 
                event.getOrderId(), event.getStatus());

        // Step 1: Query database for the order
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            
            // Step 2 & 3: Check Kafka feedback and update Order entity status
            if ("RESERVED".equals(event.getStatus())) {
                order.setStatus(OrderStatus.CONFIRMED); // Updated order instead of event
            } else {
                order.setStatus(OrderStatus.CANCELLED); // Updated order instead of event
            }
            
            // Step 4: Save the updated order back to MySQL
            orderRepository.save(order);
            
            log.info("Order ID {} status updated to {}", order.getId(), order.getStatus());
        });
    }
}
