package com.highfi.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.highfi.common.exceptions.NonExistingAccountException;
import com.highfi.model.BankAccount;
@SpringBootTest
public class BankAccountRepositoryTest {

	@Mock
	IBankAccountRepository bankAccountRepository;

	@Before
	public void init() {
		
		MockitoAnnotations.initMocks(this);

		when(bankAccountRepository.getById("nonExistingAccount")).thenThrow(NonExistingAccountException.class);
		when(bankAccountRepository.saveBankAccount(BankAccount.builder().id("newAccount").balance(new BigDecimal(0.0)).build())).thenAnswer (i -> i.getArguments()[0]);
		when(bankAccountRepository.getById("existingAccount")).thenReturn(BankAccount.builder().id("existingAccount").balance(new BigDecimal(2134.45)).build()); 
		
	}

	@Test(expected = NonExistingAccountException.class)
	public void testGetByIdRuntimeException(){
		bankAccountRepository.getById("nonExistingAccount");
	}
	
	@Test
	public void testSaveBankAccount(){
		BankAccount bankAccount = bankAccountRepository.saveBankAccount(BankAccount.builder().id("newAccount").balance(new BigDecimal(0.0)).build());
		assertEquals("newAccount",bankAccount.getId());
		assertEquals(0,bankAccount.getBalance().intValueExact(),0);
		assertEquals(0,bankAccount.getBankOperations().size());
	}

	@Test
	public void testGetById(){
		BankAccount bankAccount = bankAccountRepository.getById("existingAccount");
		assertEquals("existingAccount",bankAccount.getId());
		assertEquals(new BigDecimal(2134.45),bankAccount.getBalance());
		assertEquals(0,bankAccount.getBankOperations().size());
	}

}