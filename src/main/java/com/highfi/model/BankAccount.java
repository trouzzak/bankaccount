package com.highfi.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    private String id;
    private BigDecimal balance;
    @Builder.Default
    private List<BankOperation> bankOperations = new ArrayList<>();
}
