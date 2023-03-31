package com.folksdevbank.folksdevbank.dto.request;

import com.folksdevbank.folksdevbank.dto.CityDto;
import com.folksdevbank.folksdevbank.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseCustomerRequest {

    @NotNull
    @NotBlank(message = "Customer name must not bu blank")
    private String name;

    @NotNull
    private Integer dateOfBirth;

    @NotNull(message = "City must not be null")
    private CityDto city;

    @NotNull(message = "Address must not be null")
    private Address address;

}
