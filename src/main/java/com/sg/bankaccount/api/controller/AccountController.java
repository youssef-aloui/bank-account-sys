package com.sg.bankaccount.api.controller;

import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.model.AccountHistory;
import com.sg.bankaccount.service.AccountOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/bank")
public class AccountController {

    @Autowired
    private AccountOperationService operationService;

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity getAllAccounts() {

        return new ResponseEntity<>(
                operationService.getAllAccount(),
                HttpStatus.OK);
    }

    @GetMapping(value = "/{accountId}", produces = "application/json")
    public ResponseEntity getAccount(@PathVariable(value = "accountId") String accountId) {

        if (Objects.nonNull(accountId)) {
            Account account = this.operationService.findAccountById(accountId);
            if (Objects.isNull(account)) {
                return new ResponseEntity<>(
                        "Not Found",
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(
                    account,
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                "Null",
                HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/operation/{operationId}/{accountId}", produces = "application/json")
    public ResponseEntity operationOnAcount(@PathVariable(value = "operationId") int operationId,
                                            @PathVariable(value = "accountId") String accountId,
                                            @RequestParam(value = "amount") BigDecimal amount) {

        if (Objects.nonNull(accountId)) {
            Account account = operationService.makeOperationOnAccount(accountId, amount, operationId);

            if (Objects.isNull(account)) {
                return new ResponseEntity<>(
                        "Not Found",
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(
                    account,
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                "Null",
                HttpStatus.BAD_REQUEST);

    }

    @GetMapping(value = "/history/{accountId}", produces = "application/json")
    public ResponseEntity getAccountHistory(@PathVariable(value = "accountId") String accountId) {

        if (Objects.nonNull(accountId)) {

            List<AccountHistory> history = operationService.historyAccount(accountId);
            if (Objects.isNull(history))
                return new ResponseEntity<>(
                        "No history found",
                        HttpStatus.NOT_FOUND
                );

            return new ResponseEntity<>(
                    history,
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                "Null",
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/print", produces = "application/json")
    public ResponseEntity printStatement() {

        return new ResponseEntity<>(
                operationService.printStatement(),
                HttpStatus.OK);
    }

}

