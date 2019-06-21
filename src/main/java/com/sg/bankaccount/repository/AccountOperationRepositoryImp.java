package com.sg.bankaccount.repository;

import com.google.common.collect.Lists;
import com.sg.bankaccount.Utils.PrintMessageUtilities;
import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.model.AccountHistory;
import com.sg.bankaccount.model.OperationType;
import com.sg.bankaccount.service.AccountOperationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AccountOperationRepositoryImp implements AccountOperationRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(AccountOperationService.class);
    private List<Account> accounts;
    private List<AccountHistory> transactionHistory;

    public AccountOperationRepositoryImp() {
        accounts = new ArrayList<>();
        transactionHistory = new ArrayList<>();


        accounts.add(Account.builder()
                            .idAccount("234678")
                            .idHolder("holder1")
                            .balance(BigDecimal.ZERO)
                            .currency(Currency.getInstance("EUR"))
                            .build());
        accounts.add(Account.builder()
                            .idAccount("345890")
                            .idHolder("holder2")
                            .balance(BigDecimal.ZERO)
                            .currency(Currency.getInstance("EUR"))
                            .build());

    }

    public AccountOperationRepositoryImp(List<Account> accounts) {
        this.accounts = accounts;
        transactionHistory = new ArrayList<>();
    }

    public Optional<Account> findById(String idAccount) {

        LOGGER.info("AccountOperationRepositoryImp - findById with accountId {}", idAccount);

        return accounts.stream().filter(account -> idAccount.equals(account.getIdAccount()))
                .findFirst();

    }

    public List<Account> findAll() {

        LOGGER.info("AccountOperationRepositoryImp - findAll ");
        return accounts;
    }

    public void createHistoryOperation(Account account, BigDecimal oldBalance, OperationType operationType ) {

        LOGGER.info("AccountOperationRepositoryImp - createHistoryOperation  with account {}, oldBalance {}, operationType {} ",
                account, oldBalance, operationType);

        AccountHistory accountHistory = AccountHistory.builder()
                .idHistory(UUID.randomUUID().toString().replaceAll("-", ""))
                .account(findById(account.getIdAccount()).get())
                .operationType(operationType)
                .newBalance(account.getBalance())
                .oldBalance(oldBalance)
                .date(LocalDateTime.now())
                .build();

        transactionHistory.add(accountHistory);
    }

    public List<AccountHistory> getAccountHistory(String accountId) {

        LOGGER.info("AccountOperationRepositoryImp - getAccountHistory with accountId {}", accountId);
        return transactionHistory.stream().filter(hist -> accountId.equals(hist.getAccount().getIdAccount()))
                .collect(Collectors.toList());
    }

    public String printOperations() {

        LOGGER.info("AccountOperationRepositoryImp - printOperations ");
        final StringBuilder operationsHistory = new StringBuilder();
        operationsHistory.append(PrintMessageUtilities.HEADER);
        operationsHistory.append(PrintMessageUtilities.lineSeparator());

        transactionHistory.stream().forEach(accountHistory -> {
            operationsHistory.append(accountHistory.getDate()).append(PrintMessageUtilities.SEPARATOR);
            operationsHistory.append(accountHistory.getOperationType()).append(PrintMessageUtilities.SEPARATOR);
            operationsHistory.append(accountHistory.getOldBalance()).append(PrintMessageUtilities.SEPARATOR);
            operationsHistory.append(accountHistory.getNewBalance());
            operationsHistory.append(PrintMessageUtilities.lineSeparator());
        });

        return operationsHistory.toString();
    }
}
