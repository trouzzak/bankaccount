package com.highfi.controller;

import com.highfi.model.BankAccount;
import com.highfi.model.BankOperation;
import com.highfi.service.IBankAccountService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bankaccount")
public class BankAccountController {

    @Autowired
    private IBankAccountService bankAccountService;


    @PostMapping("/{id}")
    public BankAccount createAccount(@PathVariable(value = "id")  String id) {
        return bankAccountService.createAccount(id);
    }
    
    @GetMapping("/{id}")
    public BankAccount getAccount(@PathVariable(value = "id")  String id) {
        return bankAccountService.getAccount(id);
    }

    @PutMapping("/{id}/deposit")
    public void deposit(@PathVariable(value = "id")  String id,@RequestParam("depositAmount")   BigDecimal amount) {
        bankAccountService.deposit(id, amount);
    }

    @PutMapping("/{id}/withdrawal")
    public void withdrawal(@PathVariable(value = "id")  String id,@RequestParam(value = "withdrawalAmount")   BigDecimal amount) {
        bankAccountService.withdrawal(id, amount);
    }
    
    @GetMapping("/{id}/history")
    public List<BankOperation> getStatementByPeriode(@PathVariable(value = "id")  String id, @RequestParam("startDate") LocalDateTime startDate, @RequestParam("endDate") LocalDateTime endDate) {
        return bankAccountService.getStatementByPeriode(id, startDate,endDate);
    }
}
