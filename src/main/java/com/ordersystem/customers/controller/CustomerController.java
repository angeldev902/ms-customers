package com.ordersystem.customers.controller;

import com.ordersystem.customers.dto.request.CustomerUpsertRequest;
import com.ordersystem.customers.dto.response.*;
import com.ordersystem.customers.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * GET /customers
     */
    @GetMapping
    public ResponseEntity<List<CustomerSummaryResponse>> getAllCustomers() {
        return ResponseEntity.ok(
                customerService.getAllCustomers()
        );
    }

    /**
     * POST /customers
     */
    @PostMapping
    public ResponseEntity<CustomerUpsertResponse> createCustomer(
            @Valid @RequestBody CustomerUpsertRequest request
    ) {
        CustomerUpsertResponse response =
                customerService.createCustomer(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /customers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(
                customerService.getCustomerById(id)
        );
    }

    /**
     * PUT /customers/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerUpsertResponse> updateCustomer(
            @PathVariable("id") Long id,
            @Valid @RequestBody CustomerUpsertRequest request
    ) {
        CustomerUpsertResponse response =
                customerService.updateCustomer(id, request);

        return ResponseEntity.ok(
                response
        );
    }

    /**
     * DELETE /customers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerDeleteResponse> deleteCustomer(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(
                customerService.deleteCustomer(id)
        );
    }

    /**
     * Get /customers/page
     */
    @GetMapping("/page")
    public ResponseEntity<CustomerPageResponse> customerPage(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "page must be >= 0")
            int page,

            @RequestParam(defaultValue = "5")
            @Min(value = 1, message = "size must be >= 1")
            @Max(value = 100, message = "size must be <= 100")
            int size
    ) {
        CustomerPageResponse response = customerService.customerPage(page, size);

        return ResponseEntity.ok(response);
    }
}
