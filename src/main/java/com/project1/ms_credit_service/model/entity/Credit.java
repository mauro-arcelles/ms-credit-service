package com.project1.ms_credit_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "credits")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    @Id
    private String id;
    private String customerId;
    private BigDecimal amount;
    private String status;
    private BigDecimal interestRate;
    private Integer term;
    private BigDecimal monthlyPayment;
}