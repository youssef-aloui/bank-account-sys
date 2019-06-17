package com.sg.bankaccount.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;

@EqualsAndHashCode(of= {"idAccount","idHolder"})
@Builder
@Getter
@Setter
public class Account {

    String idAccount;

    String idHolder;

    BigDecimal balance;

    Currency currency;
}

