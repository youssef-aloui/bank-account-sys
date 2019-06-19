Feature: Deposit
    To allow a customer to deposit money.

    Scenario: Deposit account
        Given the list of accounts
            | idAccount | idHolder | balance | currency |
            | 12345     | 12345    | 2000.0  | EUR      |
            | 23456     | 23456    | 3000.0  | EUR      |
        When the amount to be deposited in the account 12345 3000
        Then the account balance should be 5000 12345