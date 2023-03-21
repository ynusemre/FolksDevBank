package com.folksdevbank.folksdevbank;

import com.folksdevbank.folksdevbank.model.Account;
import com.folksdevbank.folksdevbank.model.City;
import com.folksdevbank.folksdevbank.model.Currency;
import com.folksdevbank.folksdevbank.model.Customer;
import com.folksdevbank.folksdevbank.repository.AccountRepository;
import com.folksdevbank.folksdevbank.repository.CustomerRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootApplication
@EnableRabbit
public class FolksdevbankApplication implements CommandLineRunner {

	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;

	public FolksdevbankApplication(AccountRepository accountRepository, CustomerRepository customerRepository) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
	}


	public static void main(String[] args) {
		SpringApplication.run(FolksdevbankApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Customer c1= Customer.builder()
				.id("12345")
				.city(City.ISTANBUL)
				.name("Emre Kırgız")
				.dateOfBirth(2005)
				.address("Ev").build();
		Customer c2= Customer.builder()
				.id("12346")
				.city(City.ANKARA)
				.name("Ali Kırgız")
				.dateOfBirth(1995)
				.address("Ev").build();
		Customer c3= Customer.builder()
				.id("12347")
				.city(City.CANKIRI)
				.name("Ayse Kırgız")
				.dateOfBirth(2005)
				.address("İs").build();
		Customer c4= Customer.builder()
				.id("123478")
				.city(City.CANKIRI)
				.name("Fatma Kırgız")
				.dateOfBirth(2002)
				.address("Depo").build();

		customerRepository.saveAll(Arrays.asList(c1,c2,c3,c4));

		Account a1= Account.builder()
				.id("100")
				.currency(Currency.TRY)
				.balance(BigDecimal.valueOf(25000))
				.city(City.ISTANBUL)
				.customerId("12345").build();

		Account a2= Account.builder()
				.id("101")
				.currency(Currency.USD)
				.balance(BigDecimal.valueOf(10000))
				.city(City.KOCAELI)
				.customerId("12346").build();

		Account a3= Account.builder()
				.id("102")
				.currency(Currency.EUR)
				.balance(BigDecimal.valueOf(12000))
				.city(City.MARAS)
				.customerId("12347").build();

		accountRepository.saveAll(Arrays.asList(a1,a2,a3));

	}
}
