package com.sg.bankaccount.service;

import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.model.AccountHistory;
import com.sg.bankaccount.repository.AccountOperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.sg.bankaccount.model.OperationType.DEPOSIT;
import static com.sg.bankaccount.model.OperationType.WITHDRAW;

@Slf4j
@Component
public class AccountOperationService {

    public static final Logger LOGGER = LoggerFactory.getLogger(AccountOperationService.class);

    @Autowired
    private AccountOperationRepository operationRepository;

    public AccountOperationService(AccountOperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<Account> getAllAccount() {

        LOGGER.info("");
        return operationRepository.findAllAccount();
    }

    public Account findAccountById(String id) {

        LOGGER.info("");
        return operationRepository.findAccountById(id);
    }

    public Account makeOperationOnAccount(String accountId,
                                          BigDecimal amount,
                                          int operationId) {

        LOGGER.info("");
        if (DEPOSIT.getOperationId() == operationId)
            return depositOnAccount(accountId, amount);

        else if (WITHDRAW.getOperationId() == operationId)
            return withdrawalOnAccount(accountId, amount);

        return null;
    }

    public Account depositOnAccount(String accountId,
                                    BigDecimal amount) {

        LOGGER.info("");
        checkAmountOperation(amount);

        Account account = operationRepository.findAccountById(accountId);
        if (Objects.isNull(account))
            return null;

        BigDecimal oldAmount = account.getBalance();
        account.setBalance(account.getBalance().add(amount));
        operationRepository.createHistoryOperation(account, oldAmount, DEPOSIT);
        return account;

    }

    public Account withdrawalOnAccount(String accountId,
                                       BigDecimal amount) {

        LOGGER.info("");
        checkAmountOperation(amount);

        Account account = operationRepository.findAccountById(accountId);
        if (Objects.isNull(account))
            return null;

        BigDecimal oldAmount = account.getBalance();
        account.setBalance(account.getBalance().subtract(amount));
        operationRepository.createHistoryOperation(account, oldAmount, WITHDRAW);
        return account;
    }

    public List<AccountHistory> historyAccount(String accountId) {

        LOGGER.info("");
        return operationRepository.getAccountHistory(accountId);
    }

    public String printStatement() {

        LOGGER.info("");
        return operationRepository.printOperations();
    }

    private void checkAmountOperation(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("exception illegal amount");
        }
    }
}