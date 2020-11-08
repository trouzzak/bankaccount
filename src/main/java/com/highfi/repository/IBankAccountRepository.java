package com.highfi.repository;

import com.highfi.model.BankAccount;

public interface IBankAccountRepository  {

    BankAccount getById(String idAccount);

    BankAccount saveBankAccount(BankAccount account);
}
