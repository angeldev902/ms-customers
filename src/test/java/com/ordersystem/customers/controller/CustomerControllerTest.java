package com.ordersystem.customers.controller;

import com.ordersystem.customers.dto.request.CustomerUpsertRequest;
import com.ordersystem.customers.dto.response.*;
import com.ordersystem.customers.exception.ConflictException;
import com.ordersystem.customers.exception.GlobalExceptionHandler;
import com.ordersystem.customers.exception.ResourceNotFoundException;
import com.ordersystem.customers.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(GlobalExceptionHandler.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- GET /customers ----------
    @Test
    @DisplayName("GET /customers -> 201 Created")
    void shouldReturnCustomers() throws Exception {

        List<CustomerSummaryResponse> customers = List.of(
                new CustomerSummaryResponse(
                        1L, "Pedro Perez", "pedro@test.com", LocalDate.of(2026, 1, 15)
                )
        );

        when(customerService.getAllCustomers())
                .thenReturn(customers);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Pedro Perez"));
    }

    // ---------- GET /customers/{id} ----------
    @Test
    @DisplayName("GET /customers/{id} -> 200 OK")
    void shouldReturnCustomerById() throws Exception {

        Long customerId = 1L;

        CustomerResponse customer = new CustomerResponse(
                1L,
                "Pedro Perez",
                "pedro@test.com",
                LocalDate.of(2026, 1, 15),
                "S"
        );

        when(customerService.getCustomerById(customerId))
                .thenReturn(customer);

        mockMvc.perform(get("/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.email").value("pedro@test.com"));
    }

    @Test
    @DisplayName("GET /customers/{id} -> 404 Not Found")
    void shouldReturn404WhenCustomerNotFound() throws Exception {

        Long customerId = 99L;

        when(customerService.getCustomerById(customerId))
                .thenThrow(new ResourceNotFoundException(
                        "Customer not found with id " + customerId
                ));

        mockMvc.perform(get("/customers/{id}", customerId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Customer not found with id " + customerId));
    }

    // ---------- POST /customers ----------
    @Test
    @DisplayName("POST /customers -> 200 OK")
    void shouldCreateCustomer() throws Exception {

        CustomerUpsertRequest request = new CustomerUpsertRequest("Luis Perez", "luis@test.com");
        CustomerUpsertResponse response = new CustomerUpsertResponse(1L, "Customer created");

        when(customerService.createCustomer(any(CustomerUpsertRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.message").value("Customer created"));

    }

    @Test
    @DisplayName("POST /customers -> 409 Conflict (email exists)")
    void shouldReturn409WhenCustomerEmailAlreadyExists() throws Exception {

        CustomerUpsertRequest request = new CustomerUpsertRequest("Pedro Ruiz", "pedro@test.com");

        when(customerService.createCustomer(any()))
                .thenThrow(new ConflictException(
                        "A customer with this email already exists"
                ));

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                    .value("A customer with this email already exists"));

    }

    // ---------- PUT /customers/{id} ----------
    @Test
    @DisplayName("PUT /customers/{id} -> 200 OK")
    void shouldUpdateCustomer() throws Exception {

        Long customerId = 1L;

        CustomerUpsertRequest request = new CustomerUpsertRequest("Luis Perez Ruiz", "luis@test.com");
        CustomerUpsertResponse response = new CustomerUpsertResponse(1L, "Customer updated");

        when(customerService.updateCustomer(any(Long.class), any(CustomerUpsertRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.message").value("Customer updated"));

    }

    @Test
    @DisplayName("PUT /customers/{id} -> 409 Conflict (email exists)")
    void shouldReturn409WhenCustomerEmailAlreadyExistsUpdateCutomer() throws Exception {

        Long customerId = 1L;
        CustomerUpsertRequest request = new CustomerUpsertRequest("Pedro Ruiz", "pedro@test.com");

        when(customerService.updateCustomer(any(Long.class), any()))
                .thenThrow(new ConflictException(
                        "A customer with this email already exists"
                ));

        mockMvc.perform(put("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("A customer with this email already exists"));

    }

    @Test
    @DisplayName("PUT /customers/{id} -> 404 Not Found")
    void shouldReturn409WhenUpdatingCustomerEmailExists() throws Exception {

        Long customerId = 1L;
        CustomerUpsertRequest request = new CustomerUpsertRequest("Pedro Ruiz", "pedro@test.com");

        when(customerService.updateCustomer(any(Long.class), any()))
                .thenThrow(new ResourceNotFoundException(
                        "Customer not found with id " + customerId
                ));

        mockMvc.perform(put("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("Customer not found with id " + customerId));

    }

    // ---------- DELETE /customers/{id} ----------
    @Test
    @DisplayName("DELETE /customers/{id} -> 200 OK")
    void shouldDeleteCustomer() throws Exception {

        Long customerId = 1L;
        CustomerDeleteResponse response = new CustomerDeleteResponse("Customer deleted");

        when(customerService.deleteCustomer(customerId))
                .thenReturn(response);

        mockMvc.perform(delete("/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer deleted"));
    }

    @Test
    @DisplayName("DELETE /customers/{id} -> 404 Not Found")
    void shouldReturn404WhenCustomerNotFoundDeleteCustomer() throws Exception {

        Long customerId = 99L;

        when(customerService.deleteCustomer(customerId))
                .thenThrow(new ResourceNotFoundException(
                        "Customer not found with id " + customerId
                ));

        mockMvc.perform(delete("/customers/{id}", customerId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Customer not found with id " + customerId));
    }

    @Test
    @DisplayName("GET /customers/page -> 200 OK")
    void shouldReturnCustomerPage() throws Exception {

        int page = 0;
        int limit = 5;

        List<CustomerSummaryResponse> customers = List.of(
                new CustomerSummaryResponse(
                        1L, "Pedro Perez", "pedro@test.com", LocalDate.of(2026, 1, 15)
                )
        );
        Long total = 1L;

        CustomerPageResponse response = new CustomerPageResponse(total, customers);

        when(customerService.customerPage(page, limit))
                .thenReturn(response);

        mockMvc.perform(get("/customers/page")
                        .param("page", String.valueOf(page))
                        .param("limit", String.valueOf(limit))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total").value(total))
                .andExpect(jsonPath("$.customers[0].name").value("Pedro Perez"));
    }

}