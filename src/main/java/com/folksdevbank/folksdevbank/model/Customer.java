package com.folksdevbank.folksdevbank.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @Id
    private String id;
    private String name;
    private Integer dateOfBirth;
    private City city;
    private String address;

}
