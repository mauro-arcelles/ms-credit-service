package com.project1.ms_credit_service.business.mapper;

import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.model.CreditCardCreateRequest;
import com.project1.ms_credit_service.model.CreditCardPatchRequest;
import com.project1.ms_credit_service.model.CreditCardResponse;
import com.project1.ms_credit_service.model.entity.CreditCard;
import com.project1.ms_credit_service.model.entity.CreditCardStatus;
import com.project1.ms_credit_service.model.entity.CreditCardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CreditCardMapper {

    @Value("${application.config.credits.credit-cards.monthlyPaymentDay}")
    private Integer monthlyPaymentDay;

    @Autowired
    private Clock clock;

    public CreditCard getCreditCardCreationEntity(CreditCardCreateRequest request, String creditCardType) {
        return CreditCard.builder()
            .cardNumber(CreditCard.generateCardNumber())
            .creditLimit(request.getCreditLimit())
            .creditCardStatus(CreditCardStatus.ACTIVE)
            .creditCardType(CreditCardType.valueOf(creditCardType))
            .customerId(request.getCustomerId())
            .usedAmount(BigDecimal.ZERO)
            .monthlyPaymentDay(monthlyPaymentDay)
            .creationDate(LocalDateTime.now(clock))
            .build();
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
}
