package com.project1.ms_credit_service.business.mapper;

import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.model.CreditCardCreateRequest;
import com.project1.ms_credit_service.model.CreditCardPatchRequest;
import com.project1.ms_credit_service.model.CreditCardResponse;
import com.project1.ms_credit_service.model.entity.CreditCard;
import com.project1.ms_credit_service.model.entity.CreditCardStatus;
import com.project1.ms_credit_service.model.entity.CreditCardType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreditCardMapperTest {

    @Autowired
    private CreditCardMapper creditCardMapper;

    @Test
    void getCreditCardCreationEntity_ValidRequest_ReturnsCreditCard() {
        CreditCardCreateRequest request = new CreditCardCreateRequest();
        request.setCreditLimit(BigDecimal.valueOf(1000));
        request.setCustomerId("123");

        CreditCard result = creditCardMapper.getCreditCardCreationEntity(request, "PERSONAL");

        assertNotNull(result.getCardNumber());
        assertEquals(BigDecimal.valueOf(1000), result.getCreditLimit());
        assertEquals(CreditCardStatus.ACTIVE, result.getCreditCardStatus());
        assertEquals(CreditCardType.PERSONAL, result.getCreditCardType());
        assertEquals("123", result.getCustomerId());
        assertEquals(BigDecimal.ZERO, result.getUsedAmount());
    }

    @Test
    void getCreditCardCreationEntity_InvalidType_ThrowsBadRequestException() {
        CreditCardCreateRequest request = new CreditCardCreateRequest();

        assertThrows(BadRequestException.class, () ->
            creditCardMapper.getCreditCardCreationEntity(request, "INVALID"));
    }

    @Test
    void getCreditCardUpdateEntity_ValidRequest_UpdatesExistingCard() {
        CreditCard existingCard = new CreditCard();
        CreditCardPatchRequest request = new CreditCardPatchRequest();
        request.setCreditLimit(BigDecimal.valueOf(2000));
        request.setUsedAmount(BigDecimal.valueOf(500));

        CreditCard result = creditCardMapper.getCreditCardUpdateEntity(request, existingCard);

        assertEquals(BigDecimal.valueOf(2000), result.getCreditLimit());
        assertEquals(BigDecimal.valueOf(500), result.getUsedAmount());
    }

    @Test
    void getCreditCardResponse_ValidCard_ReturnsResponse() {
        CreditCard card = new CreditCard();
        card.setId("43123123123");
        card.setCardNumber("1234");
        card.setCreditLimit(BigDecimal.valueOf(1000));
        card.setCreditCardStatus(CreditCardStatus.ACTIVE);
        card.setCustomerId("123");
        card.setUsedAmount(BigDecimal.valueOf(100));

        CreditCardResponse response = creditCardMapper.getCreditCardResponse(card);

        assertEquals("43123123123", response.getId());
        assertEquals("1234", response.getCardNumber());
        assertEquals(BigDecimal.valueOf(1000), response.getCreditLimit());
        assertEquals("ACTIVE", response.getCreditCardStatus());
        assertEquals("123", response.getCustomerId());
        assertEquals(BigDecimal.valueOf(100), response.getUsedAmount());
    }
}
