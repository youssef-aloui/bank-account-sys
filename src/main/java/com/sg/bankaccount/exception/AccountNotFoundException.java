package com.sg.bankaccount.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super("Account not found " + message);
    }
}
