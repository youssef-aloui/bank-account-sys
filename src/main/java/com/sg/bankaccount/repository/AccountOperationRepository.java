package com.sg.bankaccount.repository;

import com.sg.bankaccount.model.Account;
import com.sg.bankaccount.model.AccountHistory;
import com.sg.bankaccount.model.OperationType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountOperationRepository {

    Optional<Account> findById(String idAccount) ;

    List<Account> findAll() ;

    void createHistoryOperation(Account account, BigDecimal oldBalance, OperationType operationType );

    List<AccountHistory> getAccountHistory(String accountId) ;

    String printOperations() ;
}
