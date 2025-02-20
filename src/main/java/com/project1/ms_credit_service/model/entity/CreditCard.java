package com.project1.ms_credit_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Random;

@Document(collection = "credit-cards")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {
    @Id
    private String id;

    private String customerId;

    private String cardNumber;

    private CreditCardType creditCardType;

    private BigDecimal creditLimit;

    private BigDecimal usedAmount;

    private CreditCardStatus creditCardStatus;

    public static String generateCardNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder("4");
        for (int i = 0; i < 15; i++) {
            number.append(random.nextInt(10));
        }
        return number.toString();
    }
}
