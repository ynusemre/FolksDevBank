package com.folksdevbank.folksdevbank.dto.converter;

import com.folksdevbank.folksdevbank.dto.AccountDto;
import com.folksdevbank.folksdevbank.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoConverter {

    public AccountDto convertToAccountDto(Account from){
        return AccountDto.builder()
                .id(from.getId())
                .customerId(from.getCustomerId())
                .balance(from.getBalance())
                .currency(from.getCurrency()).build();
    }
}
