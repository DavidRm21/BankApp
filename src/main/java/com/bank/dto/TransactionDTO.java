package com.bank.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionDTO {

    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}
