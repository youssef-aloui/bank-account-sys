package com.sg.bankaccount.service;

import com.sg.bankaccount.exception.AccountNotFoundException;
import com.sg.bankaccount.exception.InvalidAmountException;
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

        LOGGER.info("AccountOperationService - getAllAccount");
        return operationRepository.findAll();
    }

    public Account findAccountById(String accountId) throws AccountNotFoundException {

        LOGGER.info("AccountOperationService - findById with accountId{}", accountId);
        return operationRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    public Account makeOperationOnAccount(String accountId,
                                          BigDecimal amount,
                                          int operationId) throws AccountNotFoundException, InvalidAmountException {

        LOGGER.info("AccountOperationService - makeOperationOnAccount with accountId{}, amount{}, operationId{} ",
                accountId, amount, operationId);

        checkAmountOperation(amount);

        if (DEPOSIT.getOperationId() == operationId)
            return depositOnAccount(accountId, amount);

        else if (WITHDRAW.getOperationId() == operationId)
            return withdrawalOnAccount(accountId, amount);

        return null;
    }

    public List<AccountHistory> historyAccount(String accountId) throws AccountNotFoundException {

        LOGGER.info("AccountOperationService - historyAccount with accountId{} ", accountId);
        if (accountId != null)
            return operationRepository.getAccountHistory(accountId);
        throw new AccountNotFoundException("");
    }

    public String printStatement() {

        LOGGER.info("AccountOperationService - historyAccount ");
        return operationRepository.printOperations();
    }

    private Account depositOnAccount(String accountId,
                                    BigDecimal amount) throws AccountNotFoundException {

        LOGGER.info("AccountOperationService - depositOnAccount with accountId{}, amount{} ", accountId, amount);

        Account account = operationRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        BigDecimal oldAmount = account.getBalance();
        account.setBalance(account.getBalance().add(amount));
        operationRepository.createHistoryOperation(account, oldAmount, DEPOSIT);
        return account;

    }

    private Account withdrawalOnAccount(String accountId,
                                       BigDecimal amount) throws AccountNotFoundException {

        LOGGER.info("AccountOperationService - withdrawalOnAccount with accountId{}, amount{} ", accountId, amount);

        Account account = operationRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        BigDecimal oldAmount = account.getBalance();
        account.setBalance(account.getBalance().subtract(amount));
        operationRepository.createHistoryOperation(account, oldAmount, WITHDRAW);
        return account;
    }

    private void checkAmountOperation(BigDecimal amount) throws InvalidAmountException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(amount.toString());
        }
    }
}
