package com.highfi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.highfi.common.enums.BankOperationType;
import com.highfi.common.exceptions.UnauthorizedOperationException;
import com.highfi.model.BankAccount;
import com.highfi.model.BankOperation;
import com.highfi.repository.IBankAccountRepository;
public class BankAccountServiceTest {

	IBankAccountService bankAccountService;
	@Mock
	IBankAccountRepository bankAccountRepository;

	BankAccount existingAccount;
	BankAccount newAccount;
	BankAccount historyStatementAccount;

	@Before
	public void init() {

		MockitoAnnotations.initMocks(this);
		
		bankAccountService = new BankAccountServiceImpl(bankAccountRepository);
		
		existingAccount = BankAccount.builder().id("existingAccountId").balance(new BigDecimal("2020.11"))
				.bankOperations(new ArrayList<>(Arrays.asList(BankOperation.builder().amount(new BigDecimal("2020.11"))
						.type(BankOperationType.DEPOSIT).build())))
				.build();

		newAccount=(BankAccount.builder().id("newAccountId").balance(new BigDecimal(0)).build());
		
		LocalDateTime firstJanuary = LocalDateTime.of(2020, 1, 1, 0, 0);
		LocalDateTime seventhNovember = LocalDateTime.of(2020, 11, 7, 0, 0);
		historyStatementAccount=BankAccount.builder().id("statementAccount").balance(new BigDecimal(1300))
				.bankOperations(new ArrayList<>(Arrays.asList(
						BankOperation.builder().amount(new BigDecimal(1000))
						.type(BankOperationType.DEPOSIT).date(firstJanuary).build(),
						BankOperation.builder().amount(new BigDecimal(200))
						.type(BankOperationType.DEPOSIT).date(seventhNovember).build(),
						BankOperation.builder().amount(new BigDecimal(200))
						.type(BankOperationType.DEPOSIT).date(seventhNovember).build(),
						BankOperation.builder().amount(new BigDecimal(100))
						.type(BankOperationType.WITHDRAWL).date(seventhNovember).build()))
						)
				.build();
		
		
		doReturn(existingAccount).when(bankAccountRepository).getById("existingAccountId");
		doReturn(newAccount).when(bankAccountRepository).saveBankAccount(any());
		doReturn(historyStatementAccount).when(bankAccountRepository).getById("statementAccount");
		

	}

	@Test
	public void testCreateAccount(){

		BankAccount result = bankAccountService.createAccount("newAccountId");

		ArgumentCaptor<BankAccount> accountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
		
		//should pass by saveBankAccount repository method, with the same arg value
		verify(bankAccountRepository).saveBankAccount(accountArgumentCaptor.capture());
		assertEquals("newAccountId",accountArgumentCaptor.getValue().getId());
		
		//Result must match new account
		assertEquals(newAccount, result);
	}
	
	@Test
	public void shouldGetAccount(){

		BankAccount result = bankAccountService.getAccount("existingAccountId");

		//should pass by getById repository method
		verify(bankAccountRepository).getById("existingAccountId");
		//Result must match existing account
		assertEquals(existingAccount, result);

	}

	@Test
	public void testDeposit(){

		bankAccountService.deposit("existingAccountId",new BigDecimal(1));

		ArgumentCaptor<BankAccount> accountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
		verify(bankAccountRepository).saveBankAccount(accountArgumentCaptor.capture());
	
		assertEquals(new BigDecimal("2021.11") , accountArgumentCaptor.getValue().getBalance());
		assertEquals(2 , accountArgumentCaptor.getValue().getBankOperations().size());

	}
	
	@Test(expected = UnauthorizedOperationException.class)
	public void testWithdrawalUnauthorizedOperationException(){
		bankAccountService.withdrawal("existingAccountId", new BigDecimal(600));
		verify(bankAccountRepository, times(0)).saveBankAccount(any());
	}

	@Test
	public void testWithdrawal(){
		bankAccountService.withdrawal("existingAccountId",new BigDecimal(20));

		ArgumentCaptor<BankAccount> accountArgumentCaptor = ArgumentCaptor.forClass(BankAccount.class);
		verify(bankAccountRepository).saveBankAccount(accountArgumentCaptor.capture());
		
		//check
		assertEquals(new BigDecimal("2000.11") , accountArgumentCaptor.getValue().getBalance());
		assertEquals(2 , accountArgumentCaptor.getValue().getBankOperations().size());
	}
	
	 @Test
	    public void testGetStatementByPeriode(){
		 	LocalDateTime firstNovember = LocalDateTime.of(2020, 11, 1,0,0);
	        LocalDateTime ninthNovember = LocalDateTime.of(2020, 11,9,0,0);
	        
	        List<BankOperation> opretations = bankAccountService.getStatementByPeriode("statementAccount", firstNovember, ninthNovember);
	        assertEquals(3 , opretations.size());
	 }
}
