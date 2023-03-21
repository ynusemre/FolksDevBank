package com.folksdevbank.folksdevbank.dto.request;

import com.folksdevbank.folksdevbank.dto.CityDto;
import com.folksdevbank.folksdevbank.model.City;
import com.folksdevbank.folksdevbank.model.Currency;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseAccountRequest {

    @NotNull
    @NotBlank(message = "Customer id must not be empty")
    private String customerId;

    @NotNull
    @Min(0)
    private BigDecimal balance;

    @NotNull(message = "City must not be null")
    private City city;

    @NotNull(message = "Currency must not be null")
    private Currency currency;

}
