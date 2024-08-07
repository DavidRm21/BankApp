package com.bank.service.impl;

import com.bank.entity.Transaction;
import com.bank.repository.ITransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BankStatementImpl implements IBankStatement{

    private final ITransactionRepository transactionRepository;

    public List<Transaction> generateStatement(String accountNumber){
        return null;
    }

}
