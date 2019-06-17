package com.sg.bankaccount.repository;

import com.sg.bankaccount.Utils.PrintMessageUtilities;
import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.model.AccountHistory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static com.sg.bankaccount.model.OperationType.DEPOSIT;
import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountOperationRepositoryImpTest {

    private AccountOperationRepositoryImp operationRepository;

    @Before
    public void setUp() {
        operationRepository = new AccountOperationRepositoryImp();
    }

    @Test
    public void findAccountById() {

        Account account1 = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(BigDecimal.ZERO)
                .currency(Currency.getInstance("EUR"))
                .build();
        Account account2 = Account.builder()
                .idAccount("345890")
                .idHolder("holder2")
                .balance(BigDecimal.ZERO)
                .currency(Currency.getInstance("EUR"))
                .build();

        List<Account> accounts = asList(account1, account2);

        Optional<Account> account = operationRepository.findById("234678");

        Assert.assertEquals(account.get(), accounts.get(0));
    }


    @Test
    public void createHistoryOperation() {

        Account account = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(BigDecimal.valueOf(2000))
                .currency(Currency.getInstance("EUR"))
                .build();

        account.setBalance(account.getBalance().add(BigDecimal.valueOf(1000)));

        AccountHistory accountHistory = AccountHistory.builder()
                .account(operationRepository.findById(account.getIdAccount()).get())
                .operationType(DEPOSIT)
                .newBalance(account.getBalance())
                .oldBalance(BigDecimal.valueOf(1000))
                .build();

        operationRepository.createHistoryOperation(account, BigDecimal.valueOf(1000), DEPOSIT);
        List<AccountHistory> historyList = operationRepository.getAccountHistory(account.getIdAccount());

        Assert.assertEquals(historyList.size(), 1);
        Assert.assertEquals(historyList.get(0).getNewBalance(), accountHistory.getNewBalance());

    }

    @Test
    public void printOperations() {

        // Given
        Account account = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(BigDecimal.valueOf(2000))
                .currency(Currency.getInstance("EUR"))
                .build();

        account.setBalance(account.getBalance().add(BigDecimal.valueOf(1000)));

        // When
        operationRepository.createHistoryOperation(account, BigDecimal.valueOf(1000), DEPOSIT);
        List<AccountHistory> historyList = operationRepository.getAccountHistory(account.getIdAccount());


        // Then
        Assert.assertEquals(operationRepository.printOperations(),
                PrintMessageUtilities.HEADER + PrintMessageUtilities.lineSeparator()
                        + historyList.get(0).getDate() + " || DEPOSIT || 1000 || 3000" + PrintMessageUtilities.lineSeparator());
    }


}