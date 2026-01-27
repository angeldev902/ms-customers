package com.ordersystem.customers.dto.response;

public class CustomerUpsertResponse {
    private Long customerId;
    private String message;

    public CustomerUpsertResponse() {
    }

    public CustomerUpsertResponse(Long customerId, String message) {
        this.customerId = customerId;
        this.message = message;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getMessage() {
        return message;
    }

}
