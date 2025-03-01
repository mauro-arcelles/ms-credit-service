package com.project1.ms_credit_service.business.mapper;

import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.model.CreditCreateRequest;
import com.project1.ms_credit_service.model.CreditPatchRequest;
import com.project1.ms_credit_service.model.CreditResponse;
import com.project1.ms_credit_service.model.entity.Credit;
import com.project1.ms_credit_service.model.entity.CreditStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class CreditMapper {

    @Autowired
    private Clock clock;

    public Credit getCreditCreationEntity(CreditCreateRequest request) {
        return Credit.builder()
            .amount(request.getAmount())
            .status(CreditStatus.ACTIVE)
            .termInMonths(request.getTermInMonths())
            .customerId(request.getCustomerId())
            .interestRate(request.getInterestRate())
            .amountPaid(BigDecimal.ZERO)
            .identifier(Credit.generateCreditIdentifier())
            .totalAmount(calculateTotalAmount(request))
            .monthlyPayment(calculateMonthlyPayment(request))
            .expectedPaymentToDate(calculateMonthlyPayment(request))
            .creationDate(LocalDateTime.now(clock))
            .build();
    }

    /**
     * Calculates total amount to pay including interest
     * @param request Credit request containing amount and interest rate
     * @return Total amount with interest, scaled to 2 decimal places
     */
    private BigDecimal calculateTotalAmount(CreditCreateRequest request) {
        return request.getAmount()
            .multiply(BigDecimal.ONE.add(
                request.getInterestRate().divide(new BigDecimal(100), 10, RoundingMode.HALF_UP)))
            .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates monthly payment amount
     * @param request Credit request containing term in months
     * @return Monthly payment amount, scaled to 2 decimal places
     */
    private BigDecimal calculateMonthlyPayment(CreditCreateRequest request) {
        return calculateTotalAmount(request)
            .divide(new BigDecimal(request.getTermInMonths()), 2, RoundingMode.HALF_UP);
    }

    public CreditResponse getCreditResponse(Credit credit) {
        CreditResponse creditResponse = new CreditResponse();
        creditResponse.setAmount(credit.getAmount());
        creditResponse.setMonthlyPayment(credit.getMonthlyPayment());
        creditResponse.setTermInMonths(credit.getTermInMonths());
        creditResponse.setStatus(credit.getStatus().toString());
        creditResponse.setInterestRate(credit.getInterestRate());
        creditResponse.setClientId(credit.getCustomerId());
        creditResponse.setAmountPaid(credit.getAmountPaid());
        creditResponse.setTotalAmount(credit.getTotalAmount());
        creditResponse.setIdentifier(credit.getIdentifier());
        creditResponse.setNextPaymentDueDate(credit.getNextPaymentDueDate());
        creditResponse.setExpectedPaymentToDate(credit.getExpectedPaymentToDate());
        return creditResponse;
    }

    public Credit getCreditUpdateEntity(CreditPatchRequest request, Credit existingCredit) {
        if (request.getAmountPaid().compareTo(existingCredit.getTotalAmount()) > 0) {
            throw new BadRequestException("Cannot update. Amount is greater than the credit totalAmount");
        }
        existingCredit.setAmountPaid(request.getAmountPaid());
        if (request.getAmountPaid().setScale(0, RoundingMode.HALF_UP).compareTo(existingCredit.getTotalAmount().setScale(0, RoundingMode.HALF_UP)) == 0) {
            existingCredit.setStatus(CreditStatus.PAID);
        }
        if (request.getNextPaymentDueDate() != null) {
            existingCredit.setNextPaymentDueDate(request.getNextPaymentDueDate());
        }
        if (request.getExpectedPaymentToDate() != null) {
            existingCredit.setExpectedPaymentToDate(request.getExpectedPaymentToDate());
        }
        return existingCredit;
    }
}
