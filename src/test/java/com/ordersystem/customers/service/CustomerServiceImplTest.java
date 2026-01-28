package com.ordersystem.customers.service;

import com.ordersystem.customers.dao.CustomerDao;
import com.ordersystem.customers.dto.request.CustomerUpsertRequest;
import com.ordersystem.customers.dto.response.*;
import com.ordersystem.customers.exception.ConflictException;
import com.ordersystem.customers.exception.ResourceNotFoundException;
import com.ordersystem.customers.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {
    @Mock
    private CustomerDao customerDao;

    @InjectMocks
    private CustomerServiceImpl customerService;

    // ---------- getAllCustomers ----------
    @Test
    @DisplayName("Should return all customers")
    void shouldReturnAllCustomers() {
        // Arrange
        List<CustomerSummaryResponse> customers = List.of(
                new CustomerSummaryResponse(1L, "Pedro Perez", "pedro@test.com", LocalDate.of(2026, 1, 15)),
                new CustomerSummaryResponse(2L, "Mariana Torres", "mariana@test.com", LocalDate.of(2026, 1, 18))
        );

        when(customerDao.findAll()).thenReturn(customers);

        // Act
        List<CustomerSummaryResponse> result = customerService.getAllCustomers();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(customers);

        verify(customerDao).findAll();
    }

    // ---------- getCustomerById
    @Test
    @DisplayName("Should return all customer by id")
    void shouldReturnCustomerById() {
        Long customerId = 1L;

        CustomerResponse customer =   new CustomerResponse(
                1L,
                "Pedro Perez",
                "pedro@test.com",
                LocalDate.of(2026, 1, 15),
                "S"
        );

        when(customerDao.findById(customerId)).thenReturn(customer);

        CustomerResponse result = customerService.getCustomerById(customerId);

        assertThat(result).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo(customerId);

        verify(customerDao).findById(customerId);
    }

    @Test
    @DisplayName("Should throw exception when customer not found")
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Arrange
        Long customerId = 99L;

        when(customerDao.findById(customerId)).thenReturn(null);

        // Act + Assert
        assertThatThrownBy(() -> customerService.getCustomerById(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer not found with id " + customerId);

        verify(customerDao).findById(customerId);
    }

    // ---------- createCustomer
    @Test
    @DisplayName("Should create customer")
    void shouldCreateCustomer() {

        Long customerId = 3L;
        CustomerUpsertRequest customerUpsertRequest = new CustomerUpsertRequest("Rosa Solis", "rosa@test.com");

        when(customerDao.createCustomer(customerUpsertRequest)).thenReturn(customerId);

        CustomerUpsertResponse result = customerService.createCustomer(customerUpsertRequest);

        assertThat(result).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo(customerId);
        assertThat(result.getMessage()).isEqualTo("Customer created");

        verify(customerDao).createCustomer(customerUpsertRequest);
        //verifyNoMoreInteractions(customerDao);

    }

    @Test
    @DisplayName("Should throw ConflictException when email already exists in createCustomer")
    void shouldThrowExceptionWhenEmailExistsCreate() {

        CustomerUpsertRequest request =
                new CustomerUpsertRequest("Pedro Perez", "pedro@test.com");

        DataAccessException dbException =
                new DataAccessException("ORA-20001: EMAIL_ALREADY_EXISTS") {};

        when(customerDao.createCustomer(request)).thenThrow(dbException);

        assertThatThrownBy(() -> customerService.createCustomer(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("A customer with this email already exists");

        verify(customerDao).createCustomer(request);
    }

    // ---------- updateCustomer
    @Test
    @DisplayName("Should update customer")
    void shouldUpdateCustomer() {

        Long customerId = 3L;
        CustomerUpsertRequest customerUpsertRequest = new CustomerUpsertRequest("Rosa Solis", "rosa@test.com");

        CustomerUpsertResponse result = customerService.updateCustomer(customerId, customerUpsertRequest);

        assertThat(result).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo(customerId);
        assertThat(result.getMessage()).isEqualTo("Customer updated");

        verify(customerDao).updateCustomer(customerId, customerUpsertRequest);
        //verifyNoMoreInteractions(customerDao);

    }

    @Test
    @DisplayName("Should throw ConflictException when email already exists in updateCustomer")
    void shouldThrowExceptionWhenEmailExistsUpdate() {

        Long customerId = 3L;
        CustomerUpsertRequest request = new CustomerUpsertRequest("Rosa Solis", "pedro@test.com");

        DataAccessException dbException = new DataAccessException("ORA-20001: EMAIL_ALREADY_EXISTS") {};

        doThrow(dbException)
            .when(customerDao)
            .updateCustomer(customerId, request);

        assertThatThrownBy(() -> customerService.updateCustomer(customerId, request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Email already exists");

        verify(customerDao).updateCustomer(customerId, request);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when customer not found updateCustomer")
    void shouldThrowExceptionWhenNotFoundCustomerUpdate() {

        Long customerId = 5L;
        CustomerUpsertRequest request = new CustomerUpsertRequest("Rosa Solis", "rosa@test.com");

        DataAccessException dbException = new DataAccessException("ORA-20002: CUSTOMER_NOT_FOUND") {};

        doThrow(dbException)
                .when(customerDao)
                .updateCustomer(customerId, request);

        assertThatThrownBy(() -> customerService.updateCustomer(customerId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer not found with id " + customerId);

        verify(customerDao).updateCustomer(customerId, request);
    }

    // ---------- deleteCustomer
    @Test
    @DisplayName("Should delete customer")
    void shouldDeleteCustomer() {
        Long customerId = 3L;

        CustomerDeleteResponse result = customerService.deleteCustomer(customerId);

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Customer deleted");

        verify(customerDao).deleteCustomer(customerId);
        //verifyNoMoreInteractions(customerDao);

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when customer not found deleteCustomer")
    void shouldThrowExceptionWhenNotFoundCustomerDeleteCustomer() {

        Long customerId = 5L;
        DataAccessException dbException = new DataAccessException("ORA-20002: CUSTOMER_NOT_FOUND") {};

        doThrow(dbException)
                .when(customerDao)
                .deleteCustomer(customerId);

        assertThatThrownBy(() -> customerService.deleteCustomer(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer not found with id " + customerId);

        verify(customerDao).deleteCustomer(customerId);
    }

    // ---------- customerPage ----------
    @Test
    @DisplayName("Should return customerPage")
    void shouldReturnCustomerPage() {
        // Arrange
        int page = 0;
        int size = 10;
        Long total = 2L;
        List<CustomerSummaryResponse> customers = List.of(
                new CustomerSummaryResponse(1L, "Pedro Perez", "pedro@test.com", LocalDate.of(2026, 1, 15)),
                new CustomerSummaryResponse(2L, "Mariana Torres", "mariana@test.com", LocalDate.of(2026, 1, 18))
        );

        CustomerPageResponse response = new CustomerPageResponse(total, customers);

        when(customerDao.customerPage(page, size)).thenReturn(response);

        // Act
        CustomerPageResponse result = customerService.customerPage(page, size);

        // Assert
        assertThat(result.getCustomers()).hasSize(2);
        assertThat(result.getCustomers()).isEqualTo(customers);
        assertThat(result.getTotal()).isEqualTo(total);

        verify(customerDao).customerPage(page, size);
    }
}
