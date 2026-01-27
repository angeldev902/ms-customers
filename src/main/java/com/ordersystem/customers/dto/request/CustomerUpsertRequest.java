package com.ordersystem.customers.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerUpsertRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(max = 150, message = "Email must be at most 150 characters")
    private String email;

    public CustomerUpsertRequest() {
    }

    public CustomerUpsertRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "CustomerUpsertRequest {" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
