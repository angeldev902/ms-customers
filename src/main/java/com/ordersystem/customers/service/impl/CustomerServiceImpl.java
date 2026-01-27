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
        log.info("Fetching all customers");
        return customerDao.findAll();
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {

        log.info("Fetching customer detail customerId={}", customerId);

        CustomerResponse customer = customerDao.findById(customerId);

        if (customer == null) {
            log.warn("Customer not found customerId={}", customerId);
            throw new ResourceNotFoundException(
                    "Customer not found with id " + customerId
            );
        }

        log.info("Customer found customerId={}", customerId);

        return customer;
    }

    @Override
    public CustomerUpsertResponse createCustomer(
            CustomerUpsertRequest request
    ) {
        try {

            log.info("Creating customer request={}", request);

            Long customerId = customerDao.createCustomer(request);

            log.info("Customer created customerId={}", customerId);

            return new CustomerUpsertResponse(
                    customerId,
                    "Customer created"
            );
        } catch (DataAccessException ex) {

            String errorMessage = ex.getMostSpecificCause().getMessage();

            if (errorMessage != null && errorMessage.contains("ORA-20001")) {
                log.warn("Email already exists email={}", request.getEmail());
                throw new ConflictException("A customer with this email already exists");
            }

            log.error("Error creating customer", ex);
            throw ex;
        }
    }

    @Override
    public CustomerUpsertResponse updateCustomer(
            Long customerId,
            CustomerUpsertRequest request
    ) {
        try {
            log.info("Updating customerId={} request={}", customerId, request);

            customerDao.updateCustomer(customerId, request);

            log.info("Customer updated customerId={}", customerId);

            return new CustomerUpsertResponse(
                    customerId,
                    "Customer updated"
            );
        } catch (DataAccessException ex) {

            String dbMessage = ex.getMostSpecificCause().getMessage();

            if (dbMessage.contains("ORA-20001")) {
                log.warn("Email already exists on update customerId={}", customerId);
                throw new ConflictException("Email already exists");
            }

            if (dbMessage.contains("ORA-20002")) {
                log.warn("Customer not found on update customerId={}", customerId);
                throw new ResourceNotFoundException(
                        "Customer not found with id " + customerId
                );
            }

            log.error("Error updating customer customerId={}", customerId, ex);
            throw ex;
        }
    }

    @Override
    public CustomerDeleteResponse deleteCustomer(
            Long customerId
    ) {
        try {
            log.info("Deleting customer customerId={}", customerId);

            customerDao.deleteCustomer(customerId);

            log.info("Customer deleted customerId={}", customerId);

            return new CustomerDeleteResponse(
                    "Customer deleted"
            );
        } catch (DataAccessException ex) {

            String dbMessage = ex.getMostSpecificCause().getMessage();

            if (dbMessage != null && dbMessage.contains("ORA-20002")) {
                log.warn("Customer not found on delete customerId={}", customerId);

                throw new ResourceNotFoundException(
                        "Customer not found with id " + customerId
                );
            }

            log.error("Error deleting customer customerId={}", customerId, ex);
            throw ex;
        }
    }

    @Override
    public CustomerPageResponse customerPage(
            int page,  int size
    ) {
        log.info("Fetching customers page={} size={}", page, size);

        CustomerPageResponse customerPageResponse = customerDao.customerPage(page, size);

        log.info("Fetched {} customers total={}",
                customerPageResponse.getCustomers().size(),
                customerPageResponse.getTotal());

        return customerPageResponse;
    }
}
