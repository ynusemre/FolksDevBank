package com.folksdevbank.folksdevbank.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneyTransferRequest {
    private String fromId;
    private String toId;
    private BigDecimal amount;

}
