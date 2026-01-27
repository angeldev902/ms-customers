package com.ordersystem.customers.service;

import com.ordersystem.customers.dto.request.CustomerUpsertRequest;
import com.ordersystem.customers.dto.response.*;

import java.util.List;

public interface CustomerService {

    List<CustomerSummaryResponse> getAllCustomers();

    CustomerResponse getCustomerById(Long customerId);

    CustomerUpsertResponse createCustomer(CustomerUpsertRequest customerUpsertRequest);

    CustomerUpsertResponse updateCustomer(Long customerId, CustomerUpsertRequest customerUpsertRequest);

    CustomerDeleteResponse deleteCustomer(Long customerId);

    CustomerPageResponse customerPage(int page,  int size);
}
