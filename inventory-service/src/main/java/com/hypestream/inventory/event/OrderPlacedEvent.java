package com.hypestream.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Event class matching the structure published by order-service.
 * This class is used to deserialize the JSON message payload read from Kafka.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPlacedEvent implements Serializable {
    private Long orderId;
    private Long productId;
    private Integer quantity;
}
