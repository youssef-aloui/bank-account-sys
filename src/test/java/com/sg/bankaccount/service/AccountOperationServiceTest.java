package com.sg.bankaccount.service;

import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.repository.AccountOperationRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static com.sg.bankaccount.model.OperationType.DEPOSIT;
import static com.sg.bankaccount.model.OperationType.WITHDRAW;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountOperationServiceTest {

    @Mock
    private AccountOperationRepository operationRepository;

    private AccountOperationService operationService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        operationService = new AccountOperationService(operationRepository);
    }

    @Test
    public void testMakeOperationOnAccount_deposit_ok() {

        // Given
        String accountId = "234678";
        BigDecimal amount = new BigDecimal(1500);

        Account expected = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(new BigDecimal(1500))
                .currency(Currency.getInstance("EUR"))
                .build();

        when(operationRepository.findAccountById(accountId))
                .thenReturn(expected);

        // When
        Account account = operationService.makeOperationOnAccount(expected.getIdAccount(), amount, DEPOSIT.getOperationId());

        // Then
        verify(operationRepository).findAccountById(accountId);
        verify(operationRepository).createHistoryOperation(account, amount, DEPOSIT);
        Assert.assertEquals(account.getBalance(), new BigDecimal(3000));
    }

    @Test
    public void testMakeOperationOnAccount_Withdrawal_ok() {

        // Given
        String accountId = "234678";
        BigDecimal amount = new BigDecimal(1000);

        Account expected = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(new BigDecimal(1000))
                .currency(Currency.getInstance("EUR"))
                .build();

        when(operationRepository.findAccountById(accountId))
                .thenReturn(expected);

        // When
        Account account = operationService.makeOperationOnAccount(expected.getIdAccount(), amount, WITHDRAW.getOperationId());

        // Then
        verify(operationRepository).findAccountById(accountId);
        verify(operationRepository).createHistoryOperation(account, amount, WITHDRAW);
        Assert.assertEquals(account.getBalance(), BigDecimal.ZERO);
    }

    @Test
    public void testDepositOnAccount_Ko_Account_isNull() {

        // Given
        when(operationRepository.findAccountById(""))
                .thenReturn(null);

        // When
        operationService.makeOperationOnAccount("", BigDecimal.valueOf(1000), DEPOSIT.getOperationId());

        // Then
        verify(operationRepository).findAccountById("");
    }

    @Test
    public void testWithdrawalOnAccount_Ko_Account_isNull() {

        // Given
        when(operationRepository.findAccountById(""))
                .thenReturn(null);

        // When
        operationService.makeOperationOnAccount("", BigDecimal.valueOf(1000), WITHDRAW.getOperationId());

        // Then
        verify(operationRepository).findAccountById("");
    }

    @Test
    public void testMakeOperationOnAccount_Without_OperationType() {

        // Given
        when(operationRepository.findAccountById(""))
                .thenReturn(null);

        // When
        operationService.makeOperationOnAccount("", BigDecimal.valueOf(1000), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckAmountOperation_Ko() {

        operationService.makeOperationOnAccount("", BigDecimal.valueOf(-1000), WITHDRAW.getOperationId());
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("exception illegal amount");
    }
}