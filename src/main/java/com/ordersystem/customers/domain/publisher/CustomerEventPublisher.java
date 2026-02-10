package com.ordersystem.customers.domain.publisher;

public interface CustomerEventPublisher {
    void publishCustomerCreated(Long customerId);

    void publishCustomerUpdated(Long customerId);

    void publishCustomerDeleted(Long customerId);
}
