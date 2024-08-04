package com.bank.controller;

import com.bank.dto.BankResponse;
import com.bank.dto.UserRequest;
import com.bank.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Object createdAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }
}
