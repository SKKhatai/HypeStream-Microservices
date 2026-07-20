package com.hypestream.inventory.producer;

import com.hypestream.inventory.event.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryProducer {

    private static final String TOPIC = "order-stock-status";
    
    // Inject Spring's KafkaTemplate to send messages
    private final KafkaTemplate<String, InventoryReservedEvent> kafkaTemplate;

    public void sendInventoryStatus(InventoryReservedEvent event) {
        log.info("Sending InventoryReservedEvent to Kafka topic '{}' for order ID: {} with status: {}", 
                TOPIC, event.getOrderId(), event.getStatus());
        
        // Send the event using orderId as the partition key
        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);
    }
}
