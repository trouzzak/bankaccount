package com.highfi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.highfi.common.enums.BankOperationType;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class BankOperation {
    private LocalDateTime date;
    private BankOperationType type;
    private BigDecimal amount;
    private BigDecimal balance;
}
