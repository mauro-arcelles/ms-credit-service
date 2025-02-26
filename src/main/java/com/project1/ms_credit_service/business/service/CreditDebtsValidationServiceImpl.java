package com.project1.ms_credit_service.business.service;

import com.project1.ms_credit_service.business.adapter.CustomerService;
import com.project1.ms_credit_service.business.mapper.CreditDebtsValidationMapper;
import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.model.CreditDebtsResponse;
import com.project1.ms_credit_service.model.CustomerResponse;
import com.project1.ms_credit_service.model.entity.Credit;
import com.project1.ms_credit_service.model.entity.CreditCard;
import com.project1.ms_credit_service.model.entity.CustomerStatus;
import com.project1.ms_credit_service.repository.CreditCardRepository;
import com.project1.ms_credit_service.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreditDebtsValidationServiceImpl implements CreditDebtsValidationService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditDebtsValidationMapper creditDebtsValidationMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private Clock clock;

    @Override
    public Mono<CustomerResponse> validateAllDebts(CustomerResponse customer) {
        return Mono.zip(
            validateCustomerCreditDebts(customer),
            validateCustomerCreditCardDebts(customer)
        ).map(tuple -> customer);
    }

    @Override
    public Mono<CreditDebtsResponse> getCreditDebtsByCustomerId(String customerId) {
        return validateCustomerAvailability(customerId)
            .flatMap(customerResponse -> Mono.zip(
                getOverdueCredits(customerId).collectList(),
                getOverdueCreditCards(customerId).collectList()
            ).map(tuple -> creditDebtsValidationMapper.getCreditDebtsResponse(
                tuple.getT1(),
                tuple.getT2()
            )));
    }

    private Mono<CustomerResponse> validateCustomerCreditDebts(CustomerResponse customer) {
        return getOverdueCredits(customer.getId())
            .hasElements()
            .flatMap(hasDebts -> {
                if (hasDebts) {
                    return Mono.error(new BadRequestException("Cannot create CREDIT. CUSTOMER has debt on one of his credits"));
                } else {
                    return Mono.just(customer);
                }
            });
    }

    private Mono<CustomerResponse> validateCustomerCreditCardDebts(CustomerResponse customer) {
        return getOverdueCreditCards(customer.getId())
            .hasElements()
            .flatMap(hasDebts -> {
                if (hasDebts) {
                    return Mono.error(new BadRequestException("Cannot create. CUSTOMER has debt on one of his credit cards"));
                } else {
                    return Mono.just(customer);
                }
            });
    }

    private Mono<CustomerResponse> validateCustomerAvailability(String customerId) {
        return customerService.getCustomerById(customerId)
            .filter(customerResponse -> CustomerStatus.ACTIVE.toString().equals(customerResponse.getStatus()))
            .switchIfEmpty(Mono.error(new BadRequestException("Customer has INACTIVE status")));
    }

    private Flux<Credit> getOverdueCredits(String customerId) {
        return creditRepository.findByCustomerId(customerId)
            .filter(this::isOverdueCredit);
    }

    private Flux<CreditCard> getOverdueCreditCards(String customerId) {
        return creditCardRepository.findByCustomerId(customerId)
            .filter(this::isOverdueCreditCard);
    }

    private boolean isOverdueCredit(Credit credit) {
        boolean hasPassedPaymentDay = LocalDateTime.now(clock).isAfter(credit.getNextPaymentDueDate());
        boolean isCurrentDebtPaid = credit.getAmountPaid().compareTo(credit.getExpectedPaymentToDate()) >= 0;
        return hasPassedPaymentDay && !isCurrentDebtPaid;
    }

    private boolean isOverdueCreditCard(CreditCard card) {
        boolean hasAmountToPay = card.getUsedAmount().compareTo(BigDecimal.ZERO) > 0;
        boolean hasPassedPayDay = LocalDateTime.now(clock).getDayOfMonth() > card.getMonthlyPaymentDay();
        return hasAmountToPay && hasPassedPayDay;
    }
}
