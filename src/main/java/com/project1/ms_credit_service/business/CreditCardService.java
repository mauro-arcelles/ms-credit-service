package com.project1.ms_credit_service.business;

import com.project1.ms_credit_service.model.CreditCardCreateRequest;
import com.project1.ms_credit_service.model.CreditCardResponse;
import com.project1.ms_credit_service.model.CreditCardPatchRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {
    Mono<CreditCardResponse> createCreditCard(Mono<CreditCardCreateRequest> request);
    Mono<CreditCardResponse> getCreditCardByCardNumber(String cardNumber);
    Mono<CreditCardResponse> updateCreditCard(String id, Mono<CreditCardPatchRequest> request);
    Flux<CreditCardResponse> getCreditCardsByCustomerId(String customerId);
}
