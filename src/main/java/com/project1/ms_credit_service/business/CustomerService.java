package com.project1.ms_credit_service.business;

import com.project1.ms_credit_service.model.CustomerResponse;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerResponse> getCustomerById(String id);
}
