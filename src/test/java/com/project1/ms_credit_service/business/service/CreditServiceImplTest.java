package com.project1.ms_credit_service.business.service;

import com.project1.ms_credit_service.business.adapter.CustomerService;
import com.project1.ms_credit_service.business.mapper.CreditMapper;
import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.model.CreditCreateRequest;
import com.project1.ms_credit_service.model.CreditPatchRequest;
import com.project1.ms_credit_service.model.CreditResponse;
import com.project1.ms_credit_service.model.CustomerResponse;
import com.project1.ms_credit_service.model.entity.Credit;
import com.project1.ms_credit_service.repository.CreditRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@SpringBootTest
class CreditServiceImplTest {

    @MockBean
    private CreditRepository creditRepository;

    @MockBean
    private CreditMapper creditMapper;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private CreditServiceImpl creditService;

    @Test
    void createCredit_PersonalCustomerWithNoCredits_Success() {
        CreditCreateRequest createRequest = new CreditCreateRequest();
        createRequest.setCustomerId("123");

        CustomerResponse customer = new CustomerResponse();
        customer.setType("PERSONAL");

        Credit credit = new Credit();
        CreditResponse expectedResponse = new CreditResponse();

        when(customerService.getCustomerById("123")).thenReturn(Mono.just(customer));
        when(creditRepository.findByCustomerId("123")).thenReturn(Flux.empty());
        when(creditMapper.getCreditCreationEntity(createRequest)).thenReturn(credit);
        when(creditRepository.save(credit)).thenReturn(Mono.just(credit));
        when(creditMapper.getCreditResponse(credit)).thenReturn(expectedResponse);

        StepVerifier.create(creditService.createCredit(Mono.just(createRequest)))
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    void createCredit_PersonalCustomerWithExistingCredit_ThrowsException() {
        CreditCreateRequest createRequest = new CreditCreateRequest();
        createRequest.setCustomerId("123");

        CustomerResponse customer = new CustomerResponse();
        customer.setType("PERSONAL");

        when(customerService.getCustomerById("123")).thenReturn(Mono.just(customer));
        when(creditRepository.findByCustomerId("123")).thenReturn(Flux.just(new Credit()));

        StepVerifier.create(creditService.createCredit(Mono.just(createRequest)))
            .expectError(BadRequestException.class)
            .verify();
    }

    @Test
    void getCreditById_ExistingCredit_Success() {
        String creditId = "123";
        Credit credit = new Credit();
        CreditResponse expectedResponse = new CreditResponse();

        when(creditRepository.findById(creditId)).thenReturn(Mono.just(credit));
        when(creditMapper.getCreditResponse(credit)).thenReturn(expectedResponse);

        StepVerifier.create(creditService.getCreditById(creditId))
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    void getCreditById_NonExistingCredit_ThrowsException() {
        String creditId = "123";

        when(creditRepository.findById(creditId)).thenReturn(Mono.empty());

        StepVerifier.create(creditService.getCreditById(creditId))
            .expectError(BadRequestException.class)
            .verify();
    }

    @Test
    void createCredit_NonPersonalCustomer_Success() {
        CreditCreateRequest createRequest = new CreditCreateRequest();
        createRequest.setCustomerId("123");

        CustomerResponse customer = new CustomerResponse();
        customer.setType("BUSINESS");

        Credit credit = new Credit();
        CreditResponse expectedResponse = new CreditResponse();

        when(customerService.getCustomerById("123")).thenReturn(Mono.just(customer));
        when(creditMapper.getCreditCreationEntity(createRequest)).thenReturn(credit);
        when(creditRepository.save(credit)).thenReturn(Mono.just(credit));
        when(creditMapper.getCreditResponse(credit)).thenReturn(expectedResponse);

        StepVerifier.create(creditService.createCredit(Mono.just(createRequest)))
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    void getCreditsByCustomerId_ExistingCustomerWithCredits_Success() {
        String customerId = "123";
        CustomerResponse customer = new CustomerResponse();
        Credit credit1 = new Credit();
        Credit credit2 = new Credit();
        CreditResponse creditResponse1 = new CreditResponse();
        CreditResponse creditResponse2 = new CreditResponse();

        when(customerService.getCustomerById(customerId)).thenReturn(Mono.just(customer));
        when(creditRepository.findByCustomerId(customerId)).thenReturn(Flux.just(credit1, credit2));
        when(creditMapper.getCreditResponse(credit1)).thenReturn(creditResponse1);
        when(creditMapper.getCreditResponse(credit2)).thenReturn(creditResponse2);

        StepVerifier.create(creditService.getCreditsByCustomerId(customerId))
            .expectNext(creditResponse1, creditResponse2)
            .verifyComplete();
    }

    @Test
    void updateCreditById_ExistingCredit_Success() {
        String creditId = "123";
        Credit existingCredit = new Credit();
        CreditPatchRequest patchRequest = new CreditPatchRequest();
        Credit updatedCredit = new Credit();
        CreditResponse expectedResponse = new CreditResponse();

        when(creditRepository.findById(creditId)).thenReturn(Mono.just(existingCredit));
        when(creditMapper.getCreditUpdateEntity(patchRequest, existingCredit)).thenReturn(updatedCredit);
        when(creditRepository.save(updatedCredit)).thenReturn(Mono.just(updatedCredit));
        when(creditMapper.getCreditResponse(updatedCredit)).thenReturn(expectedResponse);

        StepVerifier.create(creditService.updateCreditById(creditId, Mono.just(patchRequest)))
            .expectNext(expectedResponse)
            .verifyComplete();
    }
}
