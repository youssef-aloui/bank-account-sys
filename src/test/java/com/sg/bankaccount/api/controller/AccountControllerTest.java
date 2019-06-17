package com.sg.bankaccount.api.controller;

import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.model.AccountHistory;
import com.sg.bankaccount.service.AccountOperationService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Currency;

import static com.sg.bankaccount.model.OperationType.DEPOSIT;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {


    private static final String URL = "/bank/";
    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @MockBean
    private AccountOperationService operationService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {

    }

    @Test
    public void getAllAccounts() throws Exception {
        // GIVEN
        Account expected1 = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(new BigDecimal(1000))
                .currency(Currency.getInstance("EUR"))
                .build();

        Account expected2 = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(new BigDecimal(1000))
                .currency(Currency.getInstance("EUR"))
                .build();

        when(operationService
                .getAllAccount())
                .thenReturn(Arrays.asList(expected1, expected2));

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/"))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(2)));
    }

    @Test
    public void getAccount() throws Exception {

        // GIVEN
        Account expected = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(new BigDecimal(1000))
                .currency(Currency.getInstance("EUR"))
                .build();

        when(operationService
                .findAccountById(expected.getIdAccount()))
                .thenReturn(expected);

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/234678"))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAccount").value("234678"));
    }

    @Test
    public void operationOnAccount() throws Exception {

        // GIVEN
        Account expected = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(new BigDecimal(2500))
                .currency(Currency.getInstance("EUR"))
                .build();

        when(operationService
                .makeOperationOnAccount(expected.getIdAccount(), BigDecimal.valueOf(1500), DEPOSIT.getOperationId()))
                .thenReturn(expected);

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.post(URL.concat("/operation/1/234678"))
                .param("amount", "1500")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAccount").value("234678"))
                .andExpect(jsonPath("$.balance").value(2500));
    }

    @Test
    public void getAccountHistory() throws Exception {

        // Given
        Account account = Account.builder()
                .idAccount("234678")
                .idHolder("holder1")
                .balance(BigDecimal.valueOf(2000))
                .currency(Currency.getInstance("EUR"))
                .build();

        account.setBalance(account.getBalance().add(BigDecimal.valueOf(1000)));

        AccountHistory accountHistory = AccountHistory.builder()
                .account(account)
                .operationType(DEPOSIT)
                .newBalance(account.getBalance())
                .oldBalance(BigDecimal.valueOf(1000))
                .build();

        when(operationService
                .historyAccount(account.getIdAccount()))
                .thenReturn(Arrays.asList(accountHistory));

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/history/234678"))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",Matchers.is(1)))
                .andExpect(jsonPath("$.[0].newBalance").value(3000));

    }

}