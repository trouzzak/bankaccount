package com.highfi.configuration;

import com.highfi.repository.IBankAccountRepository;
import com.highfi.repository.BankAccountRepository;
import com.highfi.service.IBankAccountService;
import com.highfi.service.BankAccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    IBankAccountRepository bankAccountRepository = new BankAccountRepository();

    @Bean
    public IBankAccountRepository getAccountRepository() {
        return new BankAccountRepository();
    }
    @Bean
    public IBankAccountService getAccountService() {
        return new BankAccountServiceImpl(bankAccountRepository);
    }

}
