package com.folksdevbank.folksdevbank.dto.request;

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
public class CreateCustomerRequest extends BaseCustomerRequest{

    @NotNull
    @NotBlank(message = "Customer id must not be empty")
    private String id;
}
