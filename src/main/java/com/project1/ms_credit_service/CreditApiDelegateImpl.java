package com.project1.ms_credit_service;

import com.project1.ms_credit_service.api.CreditsApiDelegate;
import com.project1.ms_credit_service.business.CreditCardService;
import com.project1.ms_credit_service.business.CreditService;
import com.project1.ms_credit_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CreditApiDelegateImpl implements CreditsApiDelegate {

    @Autowired
    private CreditService creditService;

    @Autowired
    private CreditCardService creditCardService;

    @Override
    public Mono<ResponseEntity<CreditResponse>> createCredit(Mono<CreditCreateRequest> createCreditRequest, ServerWebExchange exchange) {
        return creditService.createCredit(createCreditRequest)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c));
    }

    @Override
    public Mono<ResponseEntity<CreditCardResponse>> createCreditCredit(Mono<CreditCardCreateRequest> createCreditCardRequest, ServerWebExchange exchange) {
        return creditCardService.createCreditCard(createCreditCardRequest)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c));
    }

    @Override
    public Mono<ResponseEntity<CreditCardResponse>> getCreditCardByCardNumber(String cardNumber, ServerWebExchange exchange) {
        return creditCardService.getCreditCardByCardNumber(cardNumber).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditCardResponse>> updateCreditCard(String id, Mono<CreditCardPatchRequest> patchCreditCardRequest, ServerWebExchange exchange) {
        return creditCardService.updateCreditCard(id, patchCreditCardRequest).map(ResponseEntity::ok);
    }
}
