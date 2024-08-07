package com.bank.service.impl;

import com.bank.entity.Transaction;
import com.bank.repository.ITransactionRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankStatementImpl implements IBankStatement{

    private final ITransactionRepository transactionRepository;
    private static final String FILE = "C:\\Users\\Cristian\\Documents\\MyStatement.pdf";

    /**
     * Generates a statement for a specific account within a specified date range.
     *
     * @param accountNumber The account number for which to generate the statement.
     * @param startDate     The start date of the statement period in ISO-8601 format (yyyy-MM-dd).
     * @param endDate       The end date of the statement period in ISO-8601 format (yyyy-MM-dd).
     * @return A list of transactions for the specified account within the date range.
     */
    @Override
    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate){
        // Parse the startDate and endDate strings into LocalDate objects
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        // Retrieve all transactions from the repository, filter by account number and date range, and collect to list
        List<Transaction> transactionList = transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getCreatedAt() != null)
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber)) // Filter by account number
                .filter(transaction -> transaction.getCreatedAt().isEqual(start))
                .filter(transaction -> transaction.getCreatedAt().isEqual(end)) // Filter by date range
                .toList();
        return transactionList;
    }

    private void designStatement(List<Transaction> transactions) throws FileNotFoundException, DocumentException {
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);

        document.open();
        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("El banco APP"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("Calle 123, 45-67"));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("StartDate: "+ ""));
        // 32:17 Generating Bank Statements, Sending Bank Statements via email
    }



}
