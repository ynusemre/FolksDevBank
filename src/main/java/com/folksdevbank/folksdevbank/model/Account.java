package com.folksdevbank.folksdevbank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Account {

    @Id
    private String id;
    private String customerId;
    private BigDecimal balance;
    private City city;
    private Currency currency;

}
