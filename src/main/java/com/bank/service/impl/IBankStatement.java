package com.bank.service.impl;

import com.bank.entity.Transaction;

import java.util.List;

public interface IBankStatement {

    List<Transaction> generateStatement(String accountNumber, String startDate, String endDate);

}
