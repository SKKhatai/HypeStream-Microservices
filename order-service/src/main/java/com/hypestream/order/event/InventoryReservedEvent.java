package com.hypestream.order.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Event class matching the structure published by inventory-service.
 * Used to deserialize the stock check status reply from the "order-stock-status" topic.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReservedEvent implements Serializable {
    private Long orderId;
    private String status; // Can be "RESERVED" or "FAILED"
}
