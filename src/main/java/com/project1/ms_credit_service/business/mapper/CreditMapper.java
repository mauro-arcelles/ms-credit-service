package com.project1.ms_credit_service.business.mapper;

import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.model.CreditCreateRequest;
import com.project1.ms_credit_service.model.CreditPatchRequest;
import com.project1.ms_credit_service.model.CreditResponse;
import com.project1.ms_credit_service.model.entity.Credit;
import com.project1.ms_credit_service.model.entity.CreditStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CreditMapper {

    public Credit getCreditCreationEntity(CreditCreateRequest request) {
        Credit credit = new Credit();
        credit.setAmount(request.getAmount());
        credit.setStatus(CreditStatus.ACTIVE);
        credit.setTermInMonths(request.getTermInMonths());
        credit.setCustomerId(request.getCustomerId());
        credit.setInterestRate(request.getInterestRate());
        credit.setAmountPaid(new BigDecimal(0));
        credit.setIdentifier(Credit.generateCreditIdentifier());
        // Calculate total payment using: amount * (1 + interest_rate/100)
        // then calculate monthly payment using: totalPayment / term_months
        BigDecimal totalAmountToPay = request.getAmount()
                .multiply(BigDecimal.ONE.add(request.getInterestRate().divide(new BigDecimal(100), 10, RoundingMode.HALF_UP)));
        BigDecimal monthlyPayment = totalAmountToPay.divide(new BigDecimal(request.getTermInMonths()), 2, RoundingMode.HALF_UP);

        totalAmountToPay = totalAmountToPay.setScale(2, RoundingMode.HALF_UP);
        credit.setTotalAmount(totalAmountToPay);

        monthlyPayment = monthlyPayment.setScale(2, RoundingMode.HALF_UP);
        credit.setMonthlyPayment(monthlyPayment);
        return credit;
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
        return existingCredit;
    }
}
