package com.sg.bankaccount.repository;

import com.sg.bankaccount.Utils.PrintMessageUtilities;
import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.model.AccountHistory;
import com.sg.bankaccount.model.OperationType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AccountOperationRepository {

    private List<Account> accounts;
    private List<AccountHistory> transactionHistory;

    public AccountOperationRepository() {
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

    public Account findAccountById(String idAccount) {
        return accounts.stream().filter(account -> idAccount.equals(account.getIdAccount()))
                .findAny()
                .orElse(null);
    }

    public List<Account> findAllAccount() {
        return accounts;
    }

    public void createHistoryOperation(Account account, BigDecimal oldBalance, OperationType operationType ) {

        AccountHistory accountHistory = AccountHistory.builder()
                .idHistory(UUID.randomUUID().toString().replaceAll("-", ""))
                .account(findAccountById(account.getIdAccount()))
                .operationType(operationType)
                .newBalance(account.getBalance())
                .oldBalance(oldBalance)
                .date(LocalDateTime.now())
                .build();

        transactionHistory.add(accountHistory);
    }

    public List<AccountHistory> getAccountHistory(String accountId) {
        return transactionHistory.stream().filter(hist -> accountId.equals(hist.getAccount().getIdAccount()))
                .collect(Collectors.toList());
    }

    public String printOperations() {
        final StringBuilder operationsHistory = new StringBuilder();
        operationsHistory.append(PrintMessageUtilities.HEADER);
        operationsHistory.append(PrintMessageUtilities.lineSeparator());


        for (AccountHistory accountHistory : this.transactionHistory) {
            operationsHistory.append(accountHistory.getDate()).append(PrintMessageUtilities.SEPARATOR);
            operationsHistory.append(accountHistory.getOperationType()).append(PrintMessageUtilities.SEPARATOR);
            operationsHistory.append(accountHistory.getOldBalance()).append(PrintMessageUtilities.SEPARATOR);
            operationsHistory.append(accountHistory.getNewBalance());
            operationsHistory.append(PrintMessageUtilities.lineSeparator());
        }

        return operationsHistory.toString();
    }
}
