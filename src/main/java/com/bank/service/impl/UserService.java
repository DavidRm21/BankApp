package com.bank.service.impl;

import com.bank.dto.BankResponse;
import com.bank.dto.CreditDebitRequest;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.UserRequest;

public interface UserService {

    Object createAccount(UserRequest userRequestU);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);

    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);

}
