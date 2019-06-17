package com.sg.bankaccount.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class AccountHistory {

    String idHistory;

    Account account;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime date;

    OperationType operationType;

    BigDecimal newBalance;

    BigDecimal oldBalance;

}

