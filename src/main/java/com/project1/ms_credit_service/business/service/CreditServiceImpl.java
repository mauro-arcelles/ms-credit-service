package com.project1.ms_credit_service.business.service;

import com.project1.ms_credit_service.business.mapper.CreditMapper;
import com.project1.ms_credit_service.business.adapter.CustomerService;
import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.model.*;
import com.project1.ms_credit_service.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private CreditMapper creditMapper;

    @Autowired
    private CustomerService customerService;

    @Override
    public Mono<CreditResponse> createCredit(Mono<CreditCreateRequest> request) {
        return request
            .flatMap(req ->
                customerService.getCustomerById(req.getCustomerId())
                    .flatMap(customer -> validatePersonalCustomerCredit(customer, req))
            )
            .map(creditMapper::getCreditCreationEntity)
            .flatMap(creditRepository::save)
            .map(creditMapper::getCreditResponse);
    }

    @Override
    public Mono<CreditResponse> getCreditById(String id) {
        return creditRepository.findById(id)
            .switchIfEmpty(Mono.error(new BadRequestException("CREDIT not found with id: " + id)))
            .map(creditMapper::getCreditResponse);
    }

    @Override
    public Mono<CreditResponse> updateCreditById(String id, Mono<CreditPatchRequest> request) {
        return creditRepository.findById(id)
            .flatMap(credit ->
                request.map(req -> creditMapper.getCreditUpdateEntity(req, credit))
            )
            .flatMap(creditRepository::save)
            .map(creditMapper::getCreditResponse);
    }

    @Override
    public Flux<CreditResponse> getCreditsByCustomerId(String customerId) {
        return customerService.getCustomerById(customerId)
            .flatMapMany(customer -> creditRepository.findByCustomerId(customerId))
            .map(creditMapper::getCreditResponse);
    }

    private Mono<CreditCreateRequest> validatePersonalCustomerCredit(CustomerResponse customer, CreditCreateRequest request) {
        if ("PERSONAL".equals(customer.getType())) {
            return creditRepository.findByCustomerId(request.getCustomerId())
                .hasElements()
                .flatMap(hasCredits -> {
                    if (hasCredits) {
                        return Mono.error(new BadRequestException("PERSONAL customers can have just one credit"));
                    }
                    return Mono.just(request);
                });
        }
        return Mono.just(request);
    }

}
