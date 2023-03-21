package com.folksdevbank.folksdevbank.dto.request;

import com.folksdevbank.folksdevbank.model.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountRequest extends BaseAccountRequest{

    @NotNull
    @NotBlank(message = "Account id must not be empty")
    private String id;
}
