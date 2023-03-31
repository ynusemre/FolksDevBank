package com.folksdevbank.folksdevbank.service;

import com.folksdevbank.folksdevbank.dto.AccountDto;
import com.folksdevbank.folksdevbank.dto.converter.AccountDtoConverter;
import com.folksdevbank.folksdevbank.dto.request.CreateAccountRequest;
import com.folksdevbank.folksdevbank.exception.CustomerNotFoundException;
import com.folksdevbank.folksdevbank.model.*;
import com.folksdevbank.folksdevbank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {
    private AccountRepository accountRepository;
    private CustomerService customerService;
    private AccountDtoConverter converter;
    private AccountService service;
    private AmqpTemplate rabbitTemplate;

    private KafkaTemplate<String,String> kafkaTemplate;

    private DirectExchange exchange;
    @BeforeEach
    public void setUp(){
        accountRepository=mock(AccountRepository.class);
        customerService=mock(CustomerService.class);
        converter=mock(AccountDtoConverter.class);

        service=new AccountService(accountRepository,customerService,converter, rabbitTemplate, kafkaTemplate, exchange);
    }

    @Test
    public void testCreateAccount_whenCustomerIdExists_shouldCreateAccountDto(){
        CreateAccountRequest request= new CreateAccountRequest("1234");
        request.setCustomerId("12345");
        request.setBalance(BigDecimal.valueOf(100));
        request.setCity(City.ISTANBUL);
        request.setCurrency(Currency.TRY);

        Customer customer= Customer.builder()
                                .id("12345")
                                .city(City.ISTANBUL)
                                .name("Emre Kaya")
                                .address(Address.builder().city(City.MANISA).postCode("456312").addressDetails("İs Adresi").build())
                                .dateOfBirth(1995).build();

        Account account = Account.builder()
                .id(request.getId())
                .balance(request.getBalance())
                .city(request.getCity())
                .customerId(request.getCustomerId())
                .currency(request.getCurrency()).build();

        AccountDto expected = AccountDto.builder()
                .customerId("12345")
                .balance(BigDecimal.valueOf(100))
                .id("1234")
                .currency(Currency.TRY)
                .build();


        when(customerService.findCustomerById("12345")).thenReturn(customer);
        when(accountRepository.save(account)).thenReturn(account);
        when(converter.convertToAccountDto(account)).thenReturn(expected);

        AccountDto result = service.createAccount(request);

        assertEquals(result,expected);

        verify(customerService).findCustomerById("12345");
        verify(accountRepository).save(account);
        verify(converter).convertToAccountDto(account);
    }

    @Test
    public void testCreateAccount_whenCustomerIdDoesNotExists_shouldThrowCustomerNotFoundException(){
        CreateAccountRequest request= new CreateAccountRequest("1234");
        request.setCustomerId("12345");
        request.setBalance(BigDecimal.valueOf(100));
        request.setCity(City.ISTANBUL);
        request.setCurrency(Currency.TRY);

        when(customerService.findCustomerById("12345")).thenThrow(new CustomerNotFoundException("text-exception"));

        assertThrows(CustomerNotFoundException.class,()->service.createAccount(request));

        verify(customerService).findCustomerById(request.getCustomerId());

        verifyNoInteractions(accountRepository);
        verifyNoInteractions(converter);
    }



    
}