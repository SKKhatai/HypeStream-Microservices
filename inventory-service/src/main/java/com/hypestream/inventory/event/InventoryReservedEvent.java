package com.hypestream.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Event published by inventory-service to Kafka after stock check.
 * This notifies the order-service whether the sneaker stock was successfully 
 * reserved or if the item is sold out.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReservedEvent implements Serializable {
    private Long orderId;
    private String status; // Can be "RESERVED" or "FAILED"
}
