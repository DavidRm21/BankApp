package com.bank.service.impl;

import com.bank.dto.*;
import com.bank.entity.User;
import com.bank.repository.IUserRepository;
import com.bank.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IEmailService emailService;
    private final TransactionServiceImpl transactionService;



    /**
     * Crea una cuenta para un nuevo usuario.
     *
     * @param userRequestU Datos del usuario para la creación de la cuenta.
     * @return Respuesta con el resultado de la operación.
     */
    @Override
    public Object createAccount(UserRequest userRequestU) {

        Map<String, Object> response;

        // Verifica si ya existe una cuenta con el correo proporcionado
        if(userRepository.existsByEmail(userRequestU.getEmail())){
            response = java.util.Map.of(
                    "responsecode", AccountUtils.ACCOUNT_EXIST_CODE,
                    "responseMessage", AccountUtils.ACCOUNT_EXIST_MESSAGE
            );

            return response;
        }

        // Crea un nuevo usuario
        User newUser = User.builder()
                .firstName(userRequestU.getFirstName())
                .lastName(userRequestU.getLastName())
                .otherName(userRequestU.getOtherName())
                .gender(userRequestU.getGender())
                .address(userRequestU.getAddress())
                .stateOfOrigin(userRequestU.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(userRequestU.getEmail())
                .phonenumber(userRequestU.getPhonenumber())
                .alternativePhoneNumber(userRequestU.getAlternativePhoneNumber())
                .status("ACTIVE")
                .accountBalance(BigDecimal.ZERO)
                .build();

        // Guarda el nuevo usuario en el repositorio
        User saveUser = userRepository.save(newUser);

        // Envía un correo electrónico de confirmación
        /*EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .subject("Account creation")
                .messageBody("Congratulations!!")
                .build();

        // Retorna la respuesta de creación de cuenta
        emailService.sendEmailAlert(emailDetails);*/

        return response = java.util.Map.of(
                "responseMessage", AccountUtils.ACCOUNT_CREATION_MESSAGE,
                "responsecode", AccountUtils.ACCOUNT_CREATION_SUCCESS,
                "accountInfo", AccountInfo.builder()
                        .accountBalance(saveUser.getAccountBalance())
                        .accountNumber(saveUser.getAccountNumber())
                        .accountName(saveUser.getFirstName() + " " + saveUser.getLastName() + " " + saveUser.getOtherName())
                        .build());
    }

    /**
     * Consulta el saldo de una cuenta.
     *
     * @param enquiryRequest Solicitud de consulta.
     * @return Respuesta con el resultado de la consulta de saldo.
     */
    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExists){
            return BankResponse.builder()
                    .responsecode("003")
                    .responseMessage("La cuenta no existe")
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .responseMessage("Cuenta encontrada")
                .responsecode("001")
                .build();
    }

    /**
     * Consulta el nombre del titular de una cuenta.
     *
     * @param enquiryRequest Solicitud de consulta.
     * @return Nombre del titular de la cuenta.
     */
    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExists){
            return "La cuenta no existe";
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();
    }

    /**
     * Acredita una cuenta con un monto especificado.
     *
     * @param creditDebitRequest Solicitud de crédito.
     * @return Respuesta con el resultado de la operación de crédito.
     */
    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        // Verificar si la cuenta existe
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExists){
            return BankResponse.builder()
                    .responsecode("003")
                    .responseMessage("La cuenta no existe")
                    .accountInfo(null)
                    .build();
        }

        // Acreditar el monto en la cuenta
        User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        // Guardar los cambios en la cuenta
        userRepository.save(userToCredit);

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("Credito")
                .amount(creditDebitRequest.getAmount())
                .status("Sucess")
                .build();

        transactionService.saveTransaction(transactionDTO);

        return BankResponse.builder()
                .responsecode("005")
                .responseMessage("Credito exitoso")
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountNumber(creditDebitRequest.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    /**
     * Debita una cuenta con un monto especificado.
     *
     * @param creditDebitRequest Solicitud de débito.
     * @return Respuesta con el resultado de la operación de débito.
     */
    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        // Verificar si la cuenta existe
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExists){
            return BankResponse.builder()
                    .responsecode("003")
                    .responseMessage("La cuenta no existe")
                    .accountInfo(null)
                    .build();
        }
        // Verificar si el monto que se intenta debitar no es mayor a lo que esta en la cuenta
        User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = creditDebitRequest.getAmount().toBigInteger();
        if(availableBalance.intValue() < debitAmount.intValue() ){
            return BankResponse.builder()
                    .responsecode("006")
                    .responseMessage("Saldo insuficiente.")
                    .accountInfo(null)
                    .build();
        } else{
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("Debito")
                    .amount(creditDebitRequest.getAmount())
                    .status("Sucess")
                    .build();

            transactionService.saveTransaction(transactionDTO);

            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
            userRepository.save(userToDebit);
            return BankResponse.builder()
                    .accountInfo(AccountInfo.builder()
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                            .build())
                    .responseMessage("La cuenta ha sido debitada exitosamente")
                    .responsecode("007")
                    .build();
        }
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        // Obtener la cuenta aa debitar
        // Checkear si el valor de la cuenta es mayor al que se va a debitar
        // debitar la cuenta
        // Obtener la cuenta a credito
        // Acreditar la cuenta
        boolean isDestinationAccountNumber = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if (!isDestinationAccountNumber){
            return BankResponse.builder()
                    .responsecode("003")
                    .responseMessage("La cuenta destino no existe")
                    .accountInfo(null)
                    .build();
        }
        User sourceAccountNumber = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        if (transferRequest.getAmount().compareTo(sourceAccountNumber.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responsecode("003")
                    .responseMessage("Saldo insuficiente")
                    .accountInfo(null)
                    .build();
        }

        sourceAccountNumber.setAccountBalance(sourceAccountNumber.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepository.save(sourceAccountNumber);
        String sourceUsername = sourceAccountNumber.getFirstName() + " " + sourceAccountNumber.getLastName();

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("Alerta de debito")
                .recipient(sourceAccountNumber.getEmail())
                .messageBody("La suma de " + transferRequest.getAmount() + " ha sido debitada correctamente. Tu saldo actual es de " + sourceAccountNumber.getAccountBalance())
                .build();

        emailService.sendEmailAlert(debitAlert);

        User destinationAccountUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
        String recipientUsername = destinationAccountUser.getFirstName() + " " + destinationAccountUser.getLastName();
        userRepository.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("Alerta de credito")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("La suma de " + transferRequest.getAmount() + " ha sido enviada a tu cuenta por " + sourceUsername + " Tu saldo actual es de " + destinationAccountUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(transferRequest.getSourceAccountNumber())
                .transactionType("Debito")
                .amount(transferRequest.getAmount())
                .status("Sucess")
                .build();

        transactionService.saveTransaction(transactionDTO);

        return BankResponse.builder()
                .responsecode("010")
                .responseMessage("La transferencia se ha realizado correctamente")
                .accountInfo(null)
                .build();
    }

    //balance Enquiry, name enquiry, credir, debit, transfer

}
