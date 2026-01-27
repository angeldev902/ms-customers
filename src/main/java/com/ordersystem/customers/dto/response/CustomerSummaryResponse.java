package com.ordersystem.customers.dto.response;

import java.time.LocalDate;

public class CustomerSummaryResponse {
    private Long customerId;
    private String name;
    private String email;
    private LocalDate createdAt;

    public CustomerSummaryResponse() {
    }

    public CustomerSummaryResponse(Long customerId, String name, String email,
                            LocalDate createdAt) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", name=" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
