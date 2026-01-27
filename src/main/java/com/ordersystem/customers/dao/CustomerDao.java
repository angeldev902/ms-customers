package com.ordersystem.customers.dao;

import com.ordersystem.customers.dto.request.CustomerUpsertRequest;
import com.ordersystem.customers.dto.response.CustomerPageResponse;
import com.ordersystem.customers.dto.response.CustomerResponse;
import com.ordersystem.customers.dto.response.CustomerSummaryResponse;

import java.util.List;

public interface CustomerDao {

    List<CustomerSummaryResponse> findAll();

    CustomerResponse findById(Long customerId);

    Long createCustomer(CustomerUpsertRequest customerUpsertRequest);

    void updateCustomer(Long customerId, CustomerUpsertRequest customerUpsertRequest);

    void deleteCustomer(Long customerId);

    CustomerPageResponse customerPage(int page,  int size);
}
