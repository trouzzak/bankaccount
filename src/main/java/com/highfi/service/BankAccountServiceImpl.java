package com.highfi.service;

import com.highfi.common.constants.BankAccountConstants;
import com.highfi.common.enums.BankOperationType;
import com.highfi.common.exceptions.UnauthorizedOperationException;
import com.highfi.model.BankAccount;
import com.highfi.model.BankOperation;
import com.highfi.repository.IBankAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BankAccountServiceImpl implements IBankAccountService {

    private IBankAccountRepository bankAccountRepository;
    public BankAccountServiceImpl(IBankAccountRepository accountRepository) {
        this.bankAccountRepository = accountRepository;
    }
    
    @Override
    public BankAccount createAccount(String idAccount) {
        return this.bankAccountRepository.saveBankAccount(BankAccount.builder().id(idAccount).build());
    }
    
    @Override
    public BankAccount getAccount(String idAccount) {
        return this.bankAccountRepository.getById(idAccount);
    }
    
    public void deposit(String idAccount, BigDecimal amount) {
        BankAccount account = this.bankAccountRepository.getById(idAccount);
        account.setBalance(account.getBalance().add(amount));
        account.getBankOperations().add(BankOperation.builder().type(BankOperationType.DEPOSIT).amount(amount)
                .date(LocalDateTime.now()).balance(account.getBalance()).build());
        this.bankAccountRepository.saveBankAccount(account);
    }

    public void withdrawal(String idAccount, BigDecimal amount) {
        BankAccount account = this.bankAccountRepository.getById(idAccount);
        
        if(account.getBalance().compareTo(amount)<0)
            throw new UnauthorizedOperationException("Withdrawing amount exceeds the balance");
        
        if(BankAccountConstants.MAX_AUTHORIZED_WITHDRAW.compareTo(amount)<0)
            throw new UnauthorizedOperationException("Withdrawing amount exceeds maximum authorized");
        
        account.setBalance(account.getBalance().add(amount.negate()));
        account.getBankOperations().add(BankOperation.builder().type(BankOperationType.WITHDRAWL).amount(amount)
                .date(LocalDateTime.now()).balance(account.getBalance()).build());
        this.bankAccountRepository.saveBankAccount(account);
    }

    @Override
    public List<BankOperation> getStatementByPeriode(String idAccount, LocalDateTime start, LocalDateTime end) {
        BankAccount account =  this.bankAccountRepository.getById(idAccount);
      return  account.getBankOperations().stream()
        .filter(operations -> operations.getDate().isAfter(start) && operations.getDate().isBefore(end))
        .collect(Collectors.toList());
    }
}
