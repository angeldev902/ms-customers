package com.ordersystem.customers.domain.event;

import java.time.Instant;

public class CustomerEvent {

    private CustomerEventType eventType;
    private Long customerId;
    private Instant occurredAt;

    public CustomerEvent(
            CustomerEventType eventType,
            Long customerId
    ) {
        this.eventType = eventType;
        this.customerId = customerId;
        this.occurredAt = Instant.now();
    }

    // getters
    public CustomerEventType getEventType() {
        return eventType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}