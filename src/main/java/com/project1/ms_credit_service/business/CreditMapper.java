package com.project1.ms_credit_service.business;

import com.project1.ms_credit_service.model.CreditCreateRequest;
import com.project1.ms_credit_service.model.CreditResponse;
import com.project1.ms_credit_service.model.entity.Credit;
import com.project1.ms_credit_service.model.entity.CreditStatus;
import org.springframework.stereotype.Component;

@Component
public class CreditMapper {

    public Credit getCreditCreationEntity(CreditCreateRequest request) {
        Credit credit = new Credit();
        credit.setAmount(request.getAmount());
        credit.setStatus(CreditStatus.ACTIVE.toString());
        credit.setTerm(request.getTerm());
        credit.setCustomerId(request.getCustomerId());
        credit.setInterestRate(request.getInterestRate());
        credit.setMonthlyPayment(request.getMonthlyPayment());
        return credit;
    }

    public CreditResponse getCreditResponse(Credit credit) {
        CreditResponse creditResponse = new CreditResponse();
        creditResponse.setAmount(credit.getAmount());
        creditResponse.setMonthlyPayment(credit.getMonthlyPayment());
        creditResponse.setTerm(credit.getTerm());
        creditResponse.setStatus(credit.getStatus());
        creditResponse.setInterestRate(credit.getInterestRate());
        creditResponse.setClientId(credit.getCustomerId());
        return creditResponse;
    }
}
