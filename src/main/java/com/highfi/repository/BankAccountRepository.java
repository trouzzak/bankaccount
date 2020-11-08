package com.highfi.repository;

import com.highfi.common.exceptions.NonExistingAccountException;
import com.highfi.model.BankAccount;

import java.util.HashMap;
import java.util.Map;

public class BankAccountRepository implements IBankAccountRepository{
    private Map<String, BankAccount> bankNoPersitenceMap = new HashMap<>();

    public BankAccount getById(String idAccount) {
        if(!bankNoPersitenceMap.containsKey(idAccount))
            throw new NonExistingAccountException("No bank account was found with this iban");
        return bankNoPersitenceMap.get(idAccount);
    }

    public BankAccount saveBankAccount(BankAccount account) {
    	bankNoPersitenceMap.put(account.getId(),account);
        return account;
    }
}
