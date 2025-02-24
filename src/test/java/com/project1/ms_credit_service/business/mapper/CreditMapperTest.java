package com.project1.ms_credit_service.business.mapper;

import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.model.CreditCreateRequest;
import com.project1.ms_credit_service.model.CreditPatchRequest;
import com.project1.ms_credit_service.model.CreditResponse;
import com.project1.ms_credit_service.model.entity.Credit;
import com.project1.ms_credit_service.model.entity.CreditStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreditMapperTest {

    @Autowired
    private CreditMapper creditMapper;

    @Test
    void getCreditCreationEntity_ValidRequest_ReturnsCredit() {
        CreditCreateRequest request = new CreditCreateRequest();
        request.setAmount(BigDecimal.valueOf(1000));
        request.setTermInMonths(12);
        request.setCustomerId("123");
        request.setInterestRate(BigDecimal.valueOf(10));

        Credit result = creditMapper.getCreditCreationEntity(request);

        assertEquals(BigDecimal.valueOf(1000), result.getAmount());
        assertEquals(CreditStatus.ACTIVE, result.getStatus());
        assertEquals(12, result.getTermInMonths());
        assertEquals("123", result.getCustomerId());
        assertEquals(BigDecimal.valueOf(10), result.getInterestRate());
        assertEquals(BigDecimal.ZERO, result.getAmountPaid());
        assertNotNull(result.getIdentifier());
        assertEquals(BigDecimal.valueOf(1100).setScale(2), result.getTotalAmount());
        assertEquals(BigDecimal.valueOf(91.67), result.getMonthlyPayment());
    }

    @Test
    void getCreditResponse_ValidCredit_ReturnsResponse() {
        Credit credit = new Credit();
        credit.setAmount(BigDecimal.valueOf(1000));
        credit.setMonthlyPayment(BigDecimal.valueOf(91.67));
        credit.setTermInMonths(12);
        credit.setStatus(CreditStatus.ACTIVE);
        credit.setInterestRate(BigDecimal.valueOf(10));
        credit.setCustomerId("123");
        credit.setAmountPaid(BigDecimal.valueOf(100));
        credit.setTotalAmount(BigDecimal.valueOf(1100));
        credit.setIdentifier("CREDIT123");

        CreditResponse response = creditMapper.getCreditResponse(credit);

        assertEquals(BigDecimal.valueOf(1000), response.getAmount());
        assertEquals(BigDecimal.valueOf(91.67), response.getMonthlyPayment());
        assertEquals(12, response.getTermInMonths());
        assertEquals("ACTIVE", response.getStatus());
        assertEquals(BigDecimal.valueOf(10), response.getInterestRate());
        assertEquals("123", response.getClientId());
        assertEquals(BigDecimal.valueOf(100), response.getAmountPaid());
        assertEquals(BigDecimal.valueOf(1100), response.getTotalAmount());
        assertEquals("CREDIT123", response.getIdentifier());
    }

    @Test
    void getCreditUpdateEntity_ValidUpdate_UpdatesExistingCredit() {
        Credit existingCredit = new Credit();
        existingCredit.setTotalAmount(BigDecimal.valueOf(1100));
        existingCredit.setStatus(CreditStatus.ACTIVE);

        CreditPatchRequest request = new CreditPatchRequest();
        request.setAmountPaid(BigDecimal.valueOf(500));

        Credit result = creditMapper.getCreditUpdateEntity(request, existingCredit);

        assertEquals(BigDecimal.valueOf(500), result.getAmountPaid());
        assertEquals(CreditStatus.ACTIVE, result.getStatus());
    }

    @Test
    void getCreditUpdateEntity_FullPayment_UpdatesStatusToPaid() {
        Credit existingCredit = new Credit();
        existingCredit.setTotalAmount(BigDecimal.valueOf(1100));
        existingCredit.setStatus(CreditStatus.ACTIVE);

        CreditPatchRequest request = new CreditPatchRequest();
        request.setAmountPaid(BigDecimal.valueOf(1100));

        Credit result = creditMapper.getCreditUpdateEntity(request, existingCredit);

        assertEquals(CreditStatus.PAID, result.getStatus());
    }

    @Test
    void getCreditUpdateEntity_ExcessiveAmount_ThrowsBadRequestException() {
        Credit existingCredit = new Credit();
        existingCredit.setTotalAmount(BigDecimal.valueOf(1100));

        CreditPatchRequest request = new CreditPatchRequest();
        request.setAmountPaid(BigDecimal.valueOf(1200));

        assertThrows(BadRequestException.class, () ->
            creditMapper.getCreditUpdateEntity(request, existingCredit));
    }
}
