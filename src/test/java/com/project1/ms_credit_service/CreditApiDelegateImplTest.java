package com.project1.ms_credit_service;

import com.project1.ms_credit_service.business.service.CreditCardService;
import com.project1.ms_credit_service.business.service.CreditService;
import com.project1.ms_credit_service.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@SpringBootTest
class CreditApiDelegateImplTest {

    @MockBean
    private CreditService creditService;

    @MockBean
    private CreditCardService creditCardService;

    @Autowired
    private CreditApiDelegateImpl creditApiDelegate;

    @Test
    void createCredit_ValidRequest_ReturnsCreatedResponse() {
        CreditCreateRequest request = new CreditCreateRequest();
        CreditResponse response = new CreditResponse();
        when(creditService.createCredit(any())).thenReturn(Mono.just(response));

        Mono<ResponseEntity<CreditResponse>> result = creditApiDelegate.createCredit(Mono.just(request), null);

        StepVerifier.create(result)
            .expectNextMatches(r -> r.getStatusCode() == HttpStatus.CREATED && r.getBody() == response)
            .verifyComplete();
    }

    @Test
    void createCreditCard_ValidRequest_ReturnsCreatedResponse() {
        CreditCardCreateRequest request = new CreditCardCreateRequest();
        CreditCardResponse response = new CreditCardResponse();
        when(creditCardService.createCreditCard(any())).thenReturn(Mono.just(response));

        Mono<ResponseEntity<CreditCardResponse>> result = creditApiDelegate.createCreditCredit(Mono.just(request), null);

        StepVerifier.create(result)
            .expectNextMatches(r -> r.getStatusCode() == HttpStatus.CREATED && r.getBody() == response)
            .verifyComplete();
    }

    @Test
    void getCreditCardByCardNumber_ValidNumber_ReturnsOkResponse() {
        CreditCardResponse response = new CreditCardResponse();
        when(creditCardService.getCreditCardByCardNumber("123")).thenReturn(Mono.just(response));

        Mono<ResponseEntity<CreditCardResponse>> result = creditApiDelegate.getCreditCardByCardNumber("123", null);

        StepVerifier.create(result)
            .expectNextMatches(r -> r.getStatusCode() == HttpStatus.OK && r.getBody() == response)
            .verifyComplete();
    }

    @Test
    void getCreditCardById_ValidId_ReturnsOkResponse() {
        CreditCardResponse response = new CreditCardResponse();
        when(creditCardService.getCreditCardById("1")).thenReturn(Mono.just(response));

        Mono<ResponseEntity<CreditCardResponse>> result = creditApiDelegate.getCreditCardById("1", null);

        StepVerifier.create(result)
            .expectNextMatches(r -> r.getStatusCode() == HttpStatus.OK && r.getBody() == response)
            .verifyComplete();
    }

    @Test
    void updateCreditCard_ValidRequest_ReturnsOkResponse() {
        CreditCardPatchRequest request = new CreditCardPatchRequest();
        CreditCardResponse response = new CreditCardResponse();
        when(creditCardService.updateCreditCard(eq("1"), any())).thenReturn(Mono.just(response));

        Mono<ResponseEntity<CreditCardResponse>> result = creditApiDelegate.updateCreditCard("1", Mono.just(request), null);

        StepVerifier.create(result)
            .expectNextMatches(r -> r.getStatusCode() == HttpStatus.OK && r.getBody() == response)
            .verifyComplete();
    }

    @Test
    void getCreditCardsByCustomerId_ValidId_ReturnsOkResponse() {
        CreditCardResponse response = new CreditCardResponse();
        when(creditCardService.getCreditCardsByCustomerId("1")).thenReturn(Flux.just(response));

        Mono<ResponseEntity<Flux<CreditCardResponse>>> result = creditApiDelegate.getCreditCardsByCustomerId("1", null);

        StepVerifier.create(result)
            .expectNextMatches(r -> r.getStatusCode() == HttpStatus.OK)
            .verifyComplete();
    }

    @Test
    void getCreditById_ValidId_ReturnsOkResponse() {
        CreditResponse response = new CreditResponse();
        when(creditService.getCreditById("1")).thenReturn(Mono.just(response));

        Mono<ResponseEntity<CreditResponse>> result = creditApiDelegate.getCreditById("1", null);

        StepVerifier.create(result)
            .expectNextMatches(r -> r.getStatusCode() == HttpStatus.OK && r.getBody() == response)
            .verifyComplete();
    }

    @Test
    void updateCreditById_ValidRequest_ReturnsOkResponse() {
        CreditPatchRequest request = new CreditPatchRequest();
        CreditResponse response = new CreditResponse();
        when(creditService.updateCreditById(eq("1"), any())).thenReturn(Mono.just(response));

        Mono<ResponseEntity<CreditResponse>> result = creditApiDelegate.updateCreditById("1", Mono.just(request), null);

        StepVerifier.create(result)
            .expectNextMatches(r -> r.getStatusCode() == HttpStatus.OK && r.getBody() == response)
            .verifyComplete();
    }

    @Test
    void getCreditsByCustomerId_ValidId_ReturnsOkResponse() {
        CreditResponse response = new CreditResponse();
        when(creditService.getCreditsByCustomerId("1")).thenReturn(Flux.just(response));

        Mono<ResponseEntity<Flux<CreditResponse>>> result = creditApiDelegate.getCreditsByCustomerId("1", null);

        StepVerifier.create(result)
            .expectNextMatches(r -> r.getStatusCode() == HttpStatus.OK)
            .verifyComplete();
    }
}
