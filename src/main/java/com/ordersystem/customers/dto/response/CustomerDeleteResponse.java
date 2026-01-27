package com.ordersystem.customers.dto.response;

public class CustomerDeleteResponse {
    private String message;

    public CustomerDeleteResponse() {
    }

    public CustomerDeleteResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
