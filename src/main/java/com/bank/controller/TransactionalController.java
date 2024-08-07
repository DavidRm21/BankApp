package com.bank.controller;

import com.bank.entity.Transaction;
import com.bank.service.impl.IBankStatement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@RequiredArgsConstructor
public class TransactionalController {

    private final IBankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                   @RequestParam String fromDate,
                                                   @RequestParam String toDate){
        return bankStatement.generateStatement(accountNumber, fromDate, toDate);
    }
}
