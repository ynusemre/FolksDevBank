package com.folksdevbank.folksdevbank.dto;

import com.folksdevbank.folksdevbank.model.Currency;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

    private String id;
    private String customerId;
    private BigDecimal balance;
    private Currency currency;
}
