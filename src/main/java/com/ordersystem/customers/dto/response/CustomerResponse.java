package com.ordersystem.customers.dto.response;

import java.time.LocalDate;

public class CustomerResponse {
    private Long customerId;
    private String name;
    private String email;
    private LocalDate createdAt;
    private String active;

    public CustomerResponse() {
    }

    public CustomerResponse(Long customerId, String name, String email,
                            LocalDate createdAt, String active) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.active = active;
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

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
