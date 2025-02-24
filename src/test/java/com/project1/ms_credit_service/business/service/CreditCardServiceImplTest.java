package com.project1.ms_credit_service.business.service;

import com.project1.ms_credit_service.business.adapter.CustomerService;
import com.project1.ms_credit_service.business.mapper.CreditCardMapper;
import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.exception.NotFoundException;
import com.project1.ms_credit_service.model.CreditCardCreateRequest;
import com.project1.ms_credit_service.model.CreditCardPatchRequest;
import com.project1.ms_credit_service.model.CreditCardResponse;
import com.project1.ms_credit_service.model.CustomerResponse;
import com.project1.ms_credit_service.model.entity.CreditCard;
import com.project1.ms_credit_service.repository.CreditCardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@SpringBootTest
class CreditCardServiceImplTest {

    @MockBean
    private CreditCardRepository creditCardRepository;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CreditCardMapper creditCardMapper;

    @Autowired
    private CreditCardServiceImpl creditCardService;

    @Test
    void createCreditCard_ActiveCustomer_Success() {
        CreditCardCreateRequest createRequest = new CreditCardCreateRequest();
        createRequest.setCustomerId("123");

        CustomerResponse customer = new CustomerResponse();
        customer.setStatus("ACTIVE");
        customer.setType("PERSONAL");

        CreditCard creditCard = new CreditCard();
        CreditCardResponse expectedResponse = new CreditCardResponse();

        when(customerService.getCustomerById("123")).thenReturn(Mono.just(customer));
        when(creditCardMapper.getCreditCardCreationEntity(createRequest, "PERSONAL")).thenReturn(creditCard);
        when(creditCardRepository.save(creditCard)).thenReturn(Mono.just(creditCard));
        when(creditCardMapper.getCreditCardResponse(creditCard)).thenReturn(expectedResponse);

        StepVerifier.create(creditCardService.createCreditCard(Mono.just(createRequest)))
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    void createCreditCard_InactiveCustomer_ThrowsBadRequestException() {
        CreditCardCreateRequest createRequest = new CreditCardCreateRequest();
        createRequest.setCustomerId("123");

        CustomerResponse customer = new CustomerResponse();
        customer.setStatus("INACTIVE");

        when(customerService.getCustomerById("123")).thenReturn(Mono.just(customer));

        StepVerifier.create(creditCardService.createCreditCard(Mono.just(createRequest)))
            .expectError(BadRequestException.class)
            .verify();
    }

    @Test
    void getCreditCardByCardNumber_ExistingCard_Success() {
        String cardNumber = "1234567890";
        CreditCard creditCard = new CreditCard();
        CreditCardResponse expectedResponse = new CreditCardResponse();

        when(creditCardRepository.findByCardNumber(cardNumber)).thenReturn(Mono.just(creditCard));
        when(creditCardMapper.getCreditCardResponse(creditCard)).thenReturn(expectedResponse);

        StepVerifier.create(creditCardService.getCreditCardByCardNumber(cardNumber))
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    void getCreditCardByCardNumber_NonExistingCard_ThrowsNotFoundException() {
        String cardNumber = "1234567890";

        when(creditCardRepository.findByCardNumber(cardNumber)).thenReturn(Mono.empty());

        StepVerifier.create(creditCardService.getCreditCardByCardNumber(cardNumber))
            .expectError(NotFoundException.class)
            .verify();
    }

    @Test
    void updateCreditCard_ExistingCard_Success() {
        String id = "123";
        CreditCardPatchRequest patchRequest = new CreditCardPatchRequest();
        CreditCard existingCard = new CreditCard();
        CreditCard updatedCard = new CreditCard();
        CreditCardResponse expectedResponse = new CreditCardResponse();

        when(creditCardRepository.findById(id)).thenReturn(Mono.just(existingCard));
        when(creditCardMapper.getCreditCardUpdateEntity(patchRequest, existingCard)).thenReturn(updatedCard);
        when(creditCardRepository.save(updatedCard)).thenReturn(Mono.just(updatedCard));
        when(creditCardMapper.getCreditCardResponse(updatedCard)).thenReturn(expectedResponse);

        StepVerifier.create(creditCardService.updateCreditCard(id, Mono.just(patchRequest)))
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    void getCreditCardsByCustomerId_Success() {
        String customerId = "123";
        CreditCard creditCard1 = new CreditCard();
        CreditCard creditCard2 = new CreditCard();
        CreditCardResponse response1 = new CreditCardResponse();
        CreditCardResponse response2 = new CreditCardResponse();

        when(creditCardRepository.findByCustomerId(customerId)).thenReturn(Flux.just(creditCard1, creditCard2));
        when(creditCardMapper.getCreditCardResponse(creditCard1)).thenReturn(response1);
        when(creditCardMapper.getCreditCardResponse(creditCard2)).thenReturn(response2);

        StepVerifier.create(creditCardService.getCreditCardsByCustomerId(customerId))
            .expectNext(response1, response2)
            .verifyComplete();
    }

    @Test
    void getCreditCardById_ExistingCard_Success() {
        String id = "123";
        CreditCard creditCard = new CreditCard();
        CreditCardResponse expectedResponse = new CreditCardResponse();

        when(creditCardRepository.findById(id)).thenReturn(Mono.just(creditCard));
        when(creditCardMapper.getCreditCardResponse(creditCard)).thenReturn(expectedResponse);

        StepVerifier.create(creditCardService.getCreditCardById(id))
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    void getCreditCardById_NonExistingCard_ThrowsNotFoundException() {
        String id = "123";

        when(creditCardRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(creditCardService.getCreditCardById(id))
            .expectError(NotFoundException.class)
            .verify();
    }
}
