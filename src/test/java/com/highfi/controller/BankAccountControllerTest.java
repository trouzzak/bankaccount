package com.highfi.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.highfi.common.enums.BankOperationType;
import com.highfi.model.BankAccount;
import com.highfi.model.BankOperation;
import com.highfi.service.IBankAccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BankAccountControllerTest {

	@Autowired
    IBankAccountService accountService;
    @Autowired
    BankAccountController accountController;
    BankAccount expectedAccount;
    BankAccount newAccount;
    
    @Configuration
    static class Config {
        @Bean
        public IBankAccountService getAccountService() {
            return mock(IBankAccountService.class);
        }
        @Bean
        public BankAccountController getAccountController() {
            return new BankAccountController();
        }
    }
    
    @Before
	public void init() {
		
		expectedAccount = BankAccount.builder().id("testedIban").balance(new BigDecimal( "2700.52"))
                .bankOperations(new ArrayList<>(Arrays.asList(BankOperation.builder().amount(new BigDecimal("2700.52"))
                        .type(BankOperationType.DEPOSIT).build())))
                .build();
		when(accountService.getAccount("testedIban")).thenReturn(expectedAccount);
		
		newAccount=(BankAccount.builder().id("newAccountId").balance(new BigDecimal(0)).build());
		
		doReturn(newAccount).when(accountService).createAccount(anyString());
		
	}

    @Test
    public void testCreateAccount(){
        BankAccount result = accountController.createAccount("newAccount");
        
        //Should pass by createAccount service method
        verify(accountService).createAccount("newAccount");
        
        //Must match expected new account
        assertEquals(newAccount, result);
        
    }

    @Test
    public void testGetAccount(){
        BankAccount result = accountController.getAccount("testedIban");
        
        //Should pass by getAccount service method
        verify(accountService).getAccount("testedIban");
        
        //Must match expected result
        assertEquals(expectedAccount  , result);
    }

    @Test
    public void testDeposit(){
        accountController.deposit("testedIban", new BigDecimal("400.5"));
        
        //Should pass by deposit mocked service method
        verify(accountService).deposit("testedIban", new BigDecimal("400.5"));
        
    }

    @Test
    public void testWithdrawal(){
        accountController.withdrawal("testedIban", new BigDecimal(50));
        //Should pass by withdrawal mocked service methode
        verify(accountService).withdrawal("testedIban", new BigDecimal(50));
    }
    
    @Test
    public void testGetStatementByPeriode(){
    	LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstNovember = LocalDateTime.of(2020, 11, 1,0,0);
		accountController.getStatementByPeriode("testedIban", firstNovember, now);
        //Should pass by getStatementByPeriode mocked service methode
        verify(accountService).getStatementByPeriode("testedIban", firstNovember, now);
    }


}