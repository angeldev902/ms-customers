package com.ordersystem.customers.service.impl;

import com.ordersystem.customers.dao.CustomerDao;
import com.ordersystem.customers.dto.request.CustomerUpsertRequest;
import com.ordersystem.customers.dto.response.*;
import com.ordersystem.customers.exception.ConflictException;
import com.ordersystem.customers.service.CustomerService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.ordersystem.customers.exception.ResourceNotFoundException;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerDao customerDao;

    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public List<CustomerSummaryResponse> getAllCustomers() {
        return customerDao.findAll();
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {

        CustomerResponse customer = customerDao.findById(customerId);

        if (customer == null) {
            throw new ResourceNotFoundException(
                    "Customer not found with id " + customerId
            );
        }

        return customer;
    }

    @Override
    public CustomerUpsertResponse createCustomer(
            CustomerUpsertRequest request
    ) {
        try {
            Long customerId = customerDao.createCustomer(request);

            return new CustomerUpsertResponse(
                    customerId,
                    "Customer created"
            );
        } catch (DataAccessException ex) {

            String errorMessage = ex.getMostSpecificCause().getMessage();

            if (errorMessage != null && errorMessage.contains("ORA-20001")) {
                throw new ConflictException("A customer with this email already exists");
            }

            throw ex;
        }
    }

    @Override
    public CustomerUpsertResponse updateCustomer(
            Long customerId,
            CustomerUpsertRequest request
    ) {
        try {
            customerDao.updateCustomer(customerId, request);

            return new CustomerUpsertResponse(
                    customerId,
                    "Customer updated"
            );
        } catch (DataAccessException ex) {

            String dbMessage = ex.getMostSpecificCause().getMessage();

            if (dbMessage == null) {
                throw ex;
            }

            if (dbMessage.contains("ORA-20001")) {
                throw new ConflictException("Email already exists");
            }

            if (dbMessage.contains("ORA-20002")) {
                throw new ResourceNotFoundException(
                        "Customer not found with id " + customerId
                );
            }

            throw ex;
        }
    }

    @Override
    public CustomerDeleteResponse deleteCustomer(
            Long customerId
    ) {
        try {
            customerDao.deleteCustomer(customerId);

            return new CustomerDeleteResponse(
                    "Customer deleted"
            );
        } catch (DataAccessException ex) {

            String dbMessage = ex.getMostSpecificCause().getMessage();

            if (dbMessage != null && dbMessage.contains("ORA-20002")) {
                throw new ResourceNotFoundException(
                        "Customer not found with id " + customerId
                );
            }

            throw ex;
        }
    }

    @Override
    public CustomerPageResponse customerPage(
            int page,  int size
    ) {
        log.info("Fetching customers page={}, size={}", page, size);

        CustomerPageResponse customerPageResponse = customerDao.customerPage(page, size);

        return customerPageResponse;
    }
}
