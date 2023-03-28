package com.folksdevbank.folksdevbank.repository;

import com.folksdevbank.folksdevbank.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,String> {
}
