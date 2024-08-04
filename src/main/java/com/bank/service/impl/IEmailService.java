package com.bank.service.impl;

import com.bank.dto.EmailDetails;

public interface IEmailService {
    void sendEmailAlert(EmailDetails emailDetails);

}
