package com.ordersystem.customers.dto.response;

import java.util.List;

public class CustomerPageResponse {
    private Long total;
    private List<CustomerSummaryResponse> customers;

    public CustomerPageResponse() {}

    public CustomerPageResponse(Long total, List<CustomerSummaryResponse> customers) {
        this.total = total;
        this.customers = customers;
    }

    public Long getTotal() {
        return total;
    }

    public List<CustomerSummaryResponse> getCustomers() {
        return customers;
    }
}
