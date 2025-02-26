package com.project1.ms_credit_service;

import com.project1.ms_credit_service.api.CreditsApiDelegate;
import com.project1.ms_credit_service.business.service.CreditCardService;
import com.project1.ms_credit_service.business.service.CreditDebtsValidationService;
import com.project1.ms_credit_service.business.service.CreditService;
import com.project1.ms_credit_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CreditApiDelegateImpl implements CreditsApiDelegate {

    @Autowired
    private CreditService creditService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private CreditDebtsValidationService debtsValidationService;

    @Override
    public Mono<ResponseEntity<CreditResponse>> createCredit(Mono<CreditCreateRequest> createCreditRequest, ServerWebExchange exchange) {
        return creditService.createCredit(createCreditRequest)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @Override
    public Mono<ResponseEntity<CreditCardResponse>> createCreditCredit(Mono<CreditCardCreateRequest> createCreditCardRequest, ServerWebExchange exchange) {
        return creditCardService.createCreditCard(createCreditCardRequest)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @Override
    public Mono<ResponseEntity<CreditCardResponse>> getCreditCardByCardNumber(String cardNumber, ServerWebExchange exchange) {
        return creditCardService.getCreditCardByCardNumber(cardNumber)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditCardResponse>> getCreditCardById(String creditCardId, ServerWebExchange exchange) {
        return creditCardService.getCreditCardById(creditCardId)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditCardResponse>> updateCreditCard(String id, Mono<CreditCardPatchRequest> patchCreditCardRequest,
                                                                     ServerWebExchange exchange) {
        return creditCardService.updateCreditCard(id, patchCreditCardRequest)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<CreditCardResponse>>> getCreditCardsByCustomerId(String customerId, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(creditCardService.getCreditCardsByCustomerId(customerId)));
    }

    @Override
    public Mono<ResponseEntity<CreditResponse>> getCreditById(String id, ServerWebExchange exchange) {
        return creditService.getCreditById(id)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditResponse>> updateCreditById(String id, Mono<CreditPatchRequest> creditPatchRequest, ServerWebExchange exchange) {
        return creditService.updateCreditById(id, creditPatchRequest)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<CreditResponse>>> getCreditsByCustomerId(String customerId, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(creditService.getCreditsByCustomerId(customerId)));
    }

    @Override
    public Mono<ResponseEntity<CreditDebtsResponse>> validateDebts(String customerId, ServerWebExchange exchange) {
        return debtsValidationService.getCreditDebtsByCustomerId(customerId).map(ResponseEntity::ok);
    }
}
