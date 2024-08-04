package com.bank.service.impl;

import com.bank.dto.AccountInfo;
import com.bank.dto.UserRequest;
import com.bank.entity.User;
import com.bank.repository.IUserRepository;
import com.bank.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final IUserRepository userRepository;

    @Override
    public Object createAccount(UserRequest userRequestU) {

        Map<String, Object> response;

        if(userRepository.existsByEmail(userRequestU.getEmail())){
            response = java.util.Map.of(
                    "responsecode", AccountUtils.ACCOUNT_EXIST_CODE,
                    "responseMessage", AccountUtils.ACCOUNT_EXIST_MESSAGE
            );

            return response;
        }

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

        User saveUser = userRepository.save(newUser);

        return response = java.util.Map.of(
                "responseMessage", AccountUtils.ACCOUNT_CREATION_MESSAGE,
                "responsecode", AccountUtils.ACCOUNT_CREATION_SUCCESS,
                "accountInfo", AccountInfo.builder()
                        .accountBalance(saveUser.getAccountBalance())
                        .accountNumber(saveUser.getAccountNumber())
                        .accountName(saveUser.getFirstName() + " " + saveUser.getLastName() + " " + saveUser.getOtherName())
                        .build());
//                BankResponse.builder()
//                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
//                .responsecode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
//                .accountInfo(AccountInfo.builder()
//                        .accountBalance(saveUser.getAccountBalance())
//                        .accountNumber(saveUser.getAccountNumber())
//                        .accountName(saveUser.getFirstName() + " " + saveUser.getLastName() + " " + saveUser.getOtherName())
//                        .build())
//                .build();
    }

}
