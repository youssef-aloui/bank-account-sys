package com.sg.bankaccount.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super("Invalid amount " + message);
    }
}
