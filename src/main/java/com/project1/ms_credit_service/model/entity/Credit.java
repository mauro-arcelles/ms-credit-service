package com.project1.ms_credit_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "credits")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    @Id
    private String id;

    private String identifier;

    private String customerId;

    private BigDecimal amount;

    private CreditStatus status;

    private BigDecimal interestRate;

    private Integer termInMonths;

    private BigDecimal monthlyPayment;

    private BigDecimal amountPaid;

    private BigDecimal totalAmount;

    private LocalDateTime nextPaymentDueDate;

    private BigDecimal expectedPaymentToDate;

    public static String generateCreditIdentifier() {
        return UUID.randomUUID().toString();
    }
}
