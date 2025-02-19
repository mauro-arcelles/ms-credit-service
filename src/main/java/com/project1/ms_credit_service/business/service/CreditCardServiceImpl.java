package com.project1.ms_credit_service.business.service;

import com.project1.ms_credit_service.business.mapper.CreditCardMapper;
import com.project1.ms_credit_service.business.adapter.CustomerService;
import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.exception.CreditCardNotFoundException;
import com.project1.ms_credit_service.model.CreditCardCreateRequest;
import com.project1.ms_credit_service.model.CreditCardResponse;
import com.project1.ms_credit_service.model.CreditCardPatchRequest;
import com.project1.ms_credit_service.model.CustomerResponse;
import com.project1.ms_credit_service.model.entity.CustomerStatus;
import com.project1.ms_credit_service.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CreditCardMapper creditCardMapper;

    @Override
    public Mono<CreditCardResponse> createCreditCard(Mono<CreditCardCreateRequest> request) {
        return request
                .flatMap(req ->
                        this.validateCustomerAvailability(req)
                                .map(customer -> creditCardMapper.getCreditCardCreationEntity(req, customer.getType()))
                )
                .flatMap(creditCardRepository::save)
                .map(creditCardMapper::getCreditCardResponse);
    }

    private Mono<CustomerResponse> validateCustomerAvailability(CreditCardCreateRequest request) {
        return customerService.getCustomerById(request.getCustomerId())
                .filter(customerResponse -> CustomerStatus.ACTIVE.toString().equals(customerResponse.getStatus()))
                .switchIfEmpty(Mono.error(new BadRequestException("Customer has INACTIVE status")));
    }

    @Override
    public Mono<CreditCardResponse> getCreditCardByCardNumber(String cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber)
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Credit card not found with card number: " + cardNumber)))
                .map(creditCardMapper::getCreditCardResponse);
    }

    @Override
    public Mono<CreditCardResponse> updateCreditCard(String id, Mono<CreditCardPatchRequest> request) {
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Credit card not found with id: " + id)))
                .flatMap(creditCard ->
                        request.map(req -> creditCardMapper.getCreditCardUpdateEntity(req, creditCard))
                )
                .flatMap(creditCardRepository::save)
                .map(creditCardMapper::getCreditCardResponse);
    }

    @Override
    public Flux<CreditCardResponse> getCreditCardsByCustomerId(String customerId) {
        return creditCardRepository.findByCustomerId(customerId)
                .map(creditCardMapper::getCreditCardResponse);
    }

}
