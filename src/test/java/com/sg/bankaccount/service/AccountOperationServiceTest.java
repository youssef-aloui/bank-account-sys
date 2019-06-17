package com.sg.bankaccount.service;

import com.sg.bankaccount.exception.AccountNotFoundException;
import com.sg.bankaccount.exception.InvalidAmountException;
import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.repository.AccountOperationRepositoryImp;
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
import java.util.Optional;

import static com.sg.bankaccount.model.OperationType.DEPOSIT;
import static com.sg.bankaccount.model.OperationType.WITHDRAW;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountOperationServiceTest {

    @Mock
    private AccountOperationRepositoryImp operationRepository;

    private AccountOperationService operationService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        operationService = new AccountOperationService(operationRepository);
    }

    @Test
    public void testMakeOperationOnAccount_deposit_ok() throws AccountNotFoundException, InvalidAmountException {

        // Given
        String accountId = "234678";
        BigDecimal amount = new BigDecimal(1500);

        Account expected = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(new BigDecimal(1500))
                .currency(Currency.getInstance("EUR"))
                .build();

        when(operationRepository.findById(accountId))
                .thenReturn(Optional.of(expected));

        // When
        Optional<Account> account = operationService.makeOperationOnAccount(expected.getIdAccount(), amount, DEPOSIT.getOperationId());

        // Then
        verify(operationRepository).findById(accountId);
        verify(operationRepository).createHistoryOperation(account.get(), amount, DEPOSIT);
        Assert.assertEquals(account.get().getBalance(), new BigDecimal(3000));
    }

    @Test
    public void testMakeOperationOnAccount_Withdrawal_ok() throws AccountNotFoundException, InvalidAmountException {

        // Given
        String accountId = "234678";
        BigDecimal amount = new BigDecimal(1000);

        Account expected = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(new BigDecimal(1000))
                .currency(Currency.getInstance("EUR"))
                .build();

        when(operationRepository.findById(accountId))
                .thenReturn(Optional.of(expected));

        // When
        Optional<Account> account = operationService.makeOperationOnAccount(expected.getIdAccount(), amount, WITHDRAW.getOperationId());

        // Then
        verify(operationRepository).findById(accountId);
        verify(operationRepository).createHistoryOperation(account.get(), amount, WITHDRAW);
        Assert.assertEquals(account.get().getBalance(), BigDecimal.ZERO);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testDepositOnAccount_Ko_Account_isNull() throws AccountNotFoundException, InvalidAmountException {

        // Given
        when(operationRepository.findById(""))
                .thenReturn(Optional.empty());

        // When
        operationService.makeOperationOnAccount("", BigDecimal.valueOf(1000), DEPOSIT.getOperationId());

        // Then
        verify(operationRepository).findById("");
    }

    @Test(expected = AccountNotFoundException.class)
    public void testWithdrawalOnAccount_Ko_Account_isNull() throws AccountNotFoundException, InvalidAmountException {

        // Given
        when(operationRepository.findById(""))
                .thenReturn(Optional.empty());

        // When
        operationService.makeOperationOnAccount("", BigDecimal.valueOf(1000), WITHDRAW.getOperationId());

        // Then
        verify(operationRepository).findById("");

    }

    @Test
    public void testMakeOperationOnAccount_Without_OperationType() throws AccountNotFoundException, InvalidAmountException {

        // Given
        when(operationRepository.findById("234678"))
                .thenReturn(Optional.empty());

        // When
        operationService.makeOperationOnAccount("234678", BigDecimal.valueOf(1000), 0);
    }

    @Test(expected = InvalidAmountException.class)
    public void testCheckAmountOperation_Ko() throws AccountNotFoundException, InvalidAmountException {

        operationService.makeOperationOnAccount("234678", BigDecimal.valueOf(-1000), WITHDRAW.getOperationId());
        exception.expect(InvalidAmountException.class);
        exception.expectMessage("exception illegal amount");
    }
}