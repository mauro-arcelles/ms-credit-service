package com.project1.ms_credit_service.business.mapper;

import com.project1.ms_credit_service.model.CreditDebtsResponse;
import com.project1.ms_credit_service.model.CreditDebtsResponseDebts;
import com.project1.ms_credit_service.model.entity.Credit;
import com.project1.ms_credit_service.model.entity.CreditCard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CreditDebtsValidationMapper {

    public CreditDebtsResponse getCreditDebtsResponse(List<Credit> credits, List<CreditCard> creditCards) {
        CreditDebtsResponse response = new CreditDebtsResponse();
        CreditDebtsResponseDebts responseDebts = new CreditDebtsResponseDebts();

        List<String> creditIds = credits.stream()
            .map(Credit::getId)
            .collect(Collectors.toList());
        List<String> creditCardIds = creditCards.stream()
            .map(CreditCard::getId)
            .collect(Collectors.toList());

        responseDebts.setCredits(creditIds);
        responseDebts.setCreditCards(creditCardIds);

        response.setDebts(responseDebts);
        return response;
    }
}
