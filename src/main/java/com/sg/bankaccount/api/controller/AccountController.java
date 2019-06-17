package com.sg.bankaccount.api.controller;

import com.sg.bankaccount.exception.AccountNotFoundException;
import com.sg.bankaccount.exception.InvalidAmountException;
import com.sg.bankaccount.exception.OperationTypeNotFoundException;
import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.model.AccountHistory;
import com.sg.bankaccount.service.AccountOperationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bank")
public class AccountController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AccountOperationService.class);

    @Autowired
    private AccountOperationService operationService;

    @GetMapping(value = "/", produces = "application/json")
    public List<Account> getAllAccounts() {

        LOGGER.info("AccountController - getAllAccounts");

        return operationService.getAllAccount();
    }

    @GetMapping(value = "/{accountId}", produces = "application/json")
    public Account getAccount(@PathVariable(value = "accountId") String accountId) throws AccountNotFoundException {

        LOGGER.info("AccountController - getAccount with accountId{} ", accountId);

        return operationService.findAccountById(accountId);
    }

    @PostMapping(value = "/operation/{operationId}/{accountId}", produces = "application/json")
    public Account operationOnAccount(@PathVariable(value = "operationId") int operationId,
                                      @PathVariable(value = "accountId") String accountId,
                                      @RequestParam(value = "amount") BigDecimal amount) throws AccountNotFoundException,
                                                                                                InvalidAmountException,
                                                                                                OperationTypeNotFoundException {

        LOGGER.info("AccountController - operationOnAccount with operationId{}, accountId{}, amount{}",
                operationId, accountId, amount);

        return operationService.makeOperationOnAccount(accountId, amount, operationId)
                .orElseThrow(() -> new OperationTypeNotFoundException(String.valueOf(operationId)));
    }

    @GetMapping(value = "/history/{accountId}", produces = "application/json")
    public List<AccountHistory> getAccountHistory(@PathVariable(value = "accountId") String accountId) throws AccountNotFoundException {

        LOGGER.info("AccountController - getAccountHistory with accountId{} ", accountId);

        return operationService.historyAccount(accountId);
    }

    @GetMapping(value = "/print", produces = "application/json")
    public String printStatement() {

        LOGGER.info("AccountController - printStatement ");

        return operationService.printStatement();
    }

}

