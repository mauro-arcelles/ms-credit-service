package com.project1.ms_credit_service.business.mapper;

import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.model.CreditCardCreateRequest;
import com.project1.ms_credit_service.model.CreditCardPatchRequest;
import com.project1.ms_credit_service.model.CreditCardResponse;
import com.project1.ms_credit_service.model.entity.CreditCard;
import com.project1.ms_credit_service.model.entity.CreditCardStatus;
import com.project1.ms_credit_service.model.entity.CreditCardType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CreditCardMapper {

    public CreditCard getCreditCardCreationEntity(CreditCardCreateRequest request, String creditCardType) {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(CreditCard.generateCardNumber());
        creditCard.setCreditLimit(request.getCreditLimit());
        creditCard.setCreditCardStatus(CreditCardStatus.ACTIVE);
        if (!isValidCreditCardType(creditCardType)) {
            throw new BadRequestException("Invalid credit card type. Should be one of: PERSONAL|BUSINESS");
        }
        creditCard.setCreditCardType(CreditCardType.valueOf(creditCardType));
        creditCard.setCustomerId(request.getCustomerId());
        creditCard.setUsedAmount(BigDecimal.valueOf(0));
        return creditCard;
    }

    public CreditCard getCreditCardUpdateEntity(CreditCardPatchRequest request, CreditCard existingCard) {
        Optional.ofNullable(request.getCreditLimit()).ifPresent(existingCard::setCreditLimit);
        Optional.ofNullable(request.getUsedAmount()).ifPresent(existingCard::setUsedAmount);
        return existingCard;
    }

    public CreditCardResponse getCreditCardResponse(CreditCard creditCard) {
        CreditCardResponse creditCardResponse = new CreditCardResponse();
        creditCardResponse.setId(creditCard.getId());
        creditCardResponse.setCardNumber(creditCard.getCardNumber());
        creditCardResponse.setCreditLimit(creditCard.getCreditLimit());
        creditCardResponse.setCreditCardStatus(creditCard.getCreditCardStatus().toString());
        creditCardResponse.setCustomerId(creditCard.getCustomerId());
        creditCardResponse.setUsedAmount(creditCard.getUsedAmount());
        return creditCardResponse;
    }

    private boolean isValidCreditCardType(String creditCardType) {
        try {
            CreditCardType.valueOf(creditCardType);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
