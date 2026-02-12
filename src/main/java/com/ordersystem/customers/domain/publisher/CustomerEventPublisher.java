package com.ordersystem.customers.domain.publisher;

public interface CustomerEventPublisher {
    void publishCustomerCreated(Long customerId, String name, String email);

    void publishCustomerUpdated(Long customerId, String name, String email);

    void publishCustomerDeleted(Long customerId, String name, String email);
}
