package com.bank.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankResponse {

    private String responsecode;
    private String responseMessage;
    private AccountInfo accountInfo;
}
