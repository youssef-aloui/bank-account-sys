package com.sg.bankaccount.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalParamException extends Exception {
    public IllegalParamException(String message) {
    }
}
