package com.ordersystem.customers.domain.event;

import java.time.Instant;

public class CustomerEvent {

    private CustomerEventType eventType;
    private Long customerId;
    private Instant occurredAt;
    private String name;
    private String email;

    public CustomerEvent(
            CustomerEventType eventType,
            Long customerId,
            String name,
            String email
    ) {
        this.eventType = eventType;
        this.customerId = customerId;
        this.occurredAt = Instant.now();
        this.name = name;
        this.email = email;
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

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}