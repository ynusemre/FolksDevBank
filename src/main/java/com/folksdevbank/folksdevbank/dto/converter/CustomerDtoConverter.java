package com.folksdevbank.folksdevbank.dto.converter;

import com.folksdevbank.folksdevbank.dto.CityDto;
import com.folksdevbank.folksdevbank.dto.CustomerDto;
import com.folksdevbank.folksdevbank.model.Customer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomerDtoConverter {

    public CustomerDto convertToCustomerDto(Customer from){
        return new CustomerDto(
                from.getId(),
                from.getName(),
                from.getDateOfBirth(),
                CityDto.valueOf(from.getCity().name()),
                from.getAddress()
        );
    }
}
