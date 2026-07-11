package com.hypestream.order.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Event published to Kafka when a new sneaker order is created in PENDING status.
 * Other services (like inventory and payment) will listen to this event to process
 * the order asynchronously.
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
