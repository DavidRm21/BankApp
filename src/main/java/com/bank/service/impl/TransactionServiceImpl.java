package com.bank.service.impl;

import com.bank.dto.TransactionDTO;
import com.bank.entity.Transaction;
import com.bank.repository.ITransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService{

    private final ITransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDTO.getTransactionType())
                .accountNumber(transactionDTO.getAccountNumber())
                .amount(transactionDTO.getAmount())
                .status("Sucess")
                .build();
        transactionRepository.save(transaction);
    }
}
