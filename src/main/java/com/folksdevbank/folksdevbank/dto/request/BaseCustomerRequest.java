package com.folksdevbank.folksdevbank.dto.request;

import com.folksdevbank.folksdevbank.dto.CityDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotNull(message = "City must not be null")
    private String address;

}
