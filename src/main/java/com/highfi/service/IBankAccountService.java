package com.highfi.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.highfi.model.BankAccount;
import com.highfi.model.BankOperation;

public interface IBankAccountService {

	BankAccount createAccount(String idAccount);
	
	BankAccount getAccount(String idAccount);
	
	void deposit(String idAccount, BigDecimal amount);

    void withdrawal(String idAccount, BigDecimal amount);

    List<BankOperation> getStatementByPeriode(String idAccount, LocalDateTime start, LocalDateTime end);
    
}
