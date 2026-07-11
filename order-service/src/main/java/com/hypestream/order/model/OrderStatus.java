package com.hypestream.order.model;

/**
 * An enum representing the lifecycle stages of a sneaker order.
 * In a high-concurrency event-driven system, orders are not confirmed instantly.
 * They start as PENDING, and are updated to CONFIRMED or CANCELLED asynchronously
 * after inventory checks and payment processing.
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    CANCELLED
}
