package com.bank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXIST_CODE= "001";
    public static final String ACCOUNT_EXIST_MESSAGE= "This user already account Created!";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created";


    public static String generateAccountNumber(){
        Year currentYear = Year.now();
        int min = 100_000;
        int max = 999_999;
        int randNumber = (int)Math.floor(Math.random() * (max-min + 1) + min);

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomNumber).toString();
    }
}
