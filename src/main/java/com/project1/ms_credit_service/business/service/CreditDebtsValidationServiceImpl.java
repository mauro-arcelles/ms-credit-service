package com.project1.ms_credit_service.business.service;

import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.model.CustomerResponse;
import com.project1.ms_credit_service.repository.CreditCardRepository;
import com.project1.ms_credit_service.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class CreditDebtsValidationServiceImpl implements CreditDebtsValidationService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private Clock clock;

    @Override
    public Mono<CustomerResponse> validateAllDebts(CustomerResponse customer) {
        return Mono.zip(
            validateCustomerCreditDebts(customer),
            validateCustomerCreditCardDebts(customer)
        ).map(tuple -> customer);
    }

    private Mono<CustomerResponse> validateCustomerCreditDebts(CustomerResponse customer) {
        return creditRepository.findByCustomerId(customer.getId())
            .filter(credit -> {
                boolean hasPassedPaymentDay = LocalDateTime.now(clock).isAfter(credit.getNextPaymentDueDate());
                boolean isCurrentDebtPaid = credit.getAmountPaid().compareTo(credit.getExpectedPaymentToDate()) >= 0;
                return hasPassedPaymentDay && !isCurrentDebtPaid;
            })
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
        return creditCardRepository.findByCustomerId(customer.getId())
            .filter(card -> {
                boolean hasAmountToPay = card.getUsedAmount().compareTo(BigDecimal.ZERO) > 0;
                boolean hasPassedPayDay = LocalDateTime.now(clock).getDayOfMonth() > card.getMonthlyPaymentDay();
                return hasAmountToPay && hasPassedPayDay;
            })
            .hasElements()
            .flatMap(hasDebts -> {
                if (hasDebts) {
                    return Mono.error(new BadRequestException("Cannot create. CUSTOMER has debt on one of his credit cards"));
                } else {
                    return Mono.just(customer);
                }
            });
    }
}
