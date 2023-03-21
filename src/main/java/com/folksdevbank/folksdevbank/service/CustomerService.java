package com.folksdevbank.folksdevbank.service;

import com.folksdevbank.folksdevbank.dto.request.CreateCustomerRequest;
import com.folksdevbank.folksdevbank.dto.CustomerDto;
import com.folksdevbank.folksdevbank.dto.request.UpdateCustomerRequest;
import com.folksdevbank.folksdevbank.dto.converter.CustomerDtoConverter;
import com.folksdevbank.folksdevbank.exception.CustomerNotFoundException;
import com.folksdevbank.folksdevbank.model.City;
import com.folksdevbank.folksdevbank.model.Customer;
import com.folksdevbank.folksdevbank.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerDtoConverter converter;

    public CustomerService(CustomerRepository customerRepository, CustomerDtoConverter converter) {
        this.customerRepository = customerRepository;
        this.converter = converter;
    }

    public CustomerDto createCustomer(CreateCustomerRequest customerRequest){
        Customer customer = Customer.builder()
                .id(customerRequest.getId())
                .address(customerRequest.getAddress())
                .name(customerRequest.getName())
                .dateOfBirth(customerRequest.getDateOfBirth())
                .city(City.valueOf(customerRequest.getCity().name())).build();
         
        return converter.convertToCustomerDto(customerRepository.save(customer)) ;
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(converter::convertToCustomerDto)
                  .collect(Collectors.toList());
    }

    protected Customer findCustomerById(String id){
        return customerRepository.findById(id)
                .orElseThrow(()-> new CustomerNotFoundException("Customer could not found by id :" + id));
    }


    public CustomerDto getCustomerById(String id) {
        return converter.convertToCustomerDto(findCustomerById(id));
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }

    public CustomerDto updateCustomer(String id, UpdateCustomerRequest customerRequest) {
        Customer customer = findCustomerById(id);
        customer.setName(customerRequest.getName());
        customer.setCity(City.valueOf(customerRequest.getCity().name()));
        customer.setDateOfBirth(customerRequest.getDateOfBirth());
        customer.setAddress(customerRequest.getAddress());

        return converter.convertToCustomerDto(customerRepository.save(customer));
    }
}
