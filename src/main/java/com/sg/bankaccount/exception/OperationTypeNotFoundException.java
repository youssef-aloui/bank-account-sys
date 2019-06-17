package com.sg.bankaccount.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OperationTypeNotFoundException extends Exception {
    public OperationTypeNotFoundException(String message) {
        super("Operation type not found " + message);
    }
}