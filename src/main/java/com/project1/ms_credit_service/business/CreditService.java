package com.project1.ms_credit_service.business;

import com.project1.ms_credit_service.model.CreditCreateRequest;
import com.project1.ms_credit_service.model.CreditPatchRequest;
import com.project1.ms_credit_service.model.CreditResponse;
import reactor.core.publisher.Mono;

public interface CreditService {
    Mono<CreditResponse> createCredit(Mono<CreditCreateRequest> request);
    Mono<CreditResponse> getCreditById(String id);
    Mono<CreditResponse> updateCreditById(String id, Mono<CreditPatchRequest> request);
}
