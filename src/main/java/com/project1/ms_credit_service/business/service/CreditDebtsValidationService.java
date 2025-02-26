package com.project1.ms_credit_service.business.service;

import com.project1.ms_credit_service.model.CreditDebtsResponse;
import com.project1.ms_credit_service.model.CustomerResponse;
import reactor.core.publisher.Mono;

public interface CreditDebtsValidationService {
    Mono<CustomerResponse> validateAllDebts(CustomerResponse response);

    Mono<CreditDebtsResponse> getCreditDebtsByCustomerId(String customerId);
}
