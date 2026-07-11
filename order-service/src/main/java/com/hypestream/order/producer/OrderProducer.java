package com.hypestream.order.producer;

import com.hypestream.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private static final String TOPIC = "order-placed";
    
    // Inject the autoconfigured KafkaTemplate from Spring Boot
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void sendOrderPlacedEvent(OrderPlacedEvent event) {
        log.info("Sending OrderPlacedEvent to Kafka topic '{}' for order ID: {}", TOPIC, event.getOrderId());
        
        // Send the event to the "order-placed" topic
        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);
    }
}
