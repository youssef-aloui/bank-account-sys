package com.sg.bdd;

import com.sg.bankaccount.exception.AccountNotFoundException;
import com.sg.bankaccount.exception.InvalidAmountException;
import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.repository.AccountOperationRepositoryImp;
import com.sg.bankaccount.service.AccountOperationService;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.junit.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.sg.bankaccount.model.OperationType.DEPOSIT;

public class DepositStepdefs implements En {

    List<Account> accountList;

    private AccountOperationRepositoryImp  operationRepository;

    private AccountOperationService operationService;

    public DepositStepdefs()  {

        Given("the list of accounts", (final DataTable accounts) -> {
            accountList = accounts.asList(Account.class);
            operationRepository = new AccountOperationRepositoryImp(accountList);
            operationService = new AccountOperationService(operationRepository);
        });

        When("the amount to be deposited in the account (.+) (.+)", (final String accountId, final String amount) -> {

            try {
                operationService.makeOperationOnAccount(accountId, new BigDecimal(amount), DEPOSIT.getOperationId());
            } catch(AccountNotFoundException e) {
            } catch(InvalidAmountException e) {
            }
        });

        Then("the account balance should be (.+) (.+)", (final String amount, final String accountId) -> {

            Optional<Account> account = accountList.stream().filter(cp -> accountId.equals(cp.getIdAccount())).findFirst();
            Assert.assertEquals(account.get().getBalance(), new BigDecimal(5000).setScale(1,BigDecimal.ROUND_HALF_UP));
        });

    }
}
