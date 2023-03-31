package com.folksdevbank.folksdevbank;

import com.folksdevbank.folksdevbank.model.*;
import com.folksdevbank.folksdevbank.model.Currency;
import com.folksdevbank.folksdevbank.repository.AccountRepository;
import com.folksdevbank.folksdevbank.repository.AddressRepository;
import com.folksdevbank.folksdevbank.repository.CustomerRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableRabbit
public class FolksdevbankApplication implements CommandLineRunner {

	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	private final AddressRepository addressRepository;

	public FolksdevbankApplication(AccountRepository accountRepository, CustomerRepository customerRepository, AddressRepository addressRepository) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
		this.addressRepository = addressRepository;
	}


	public static void main(String[] args) {
		SpringApplication.run(FolksdevbankApplication.class, args);
	}


	@Bean
	public OpenAPI customOpenAPI(@Value("${application-description}") String description,
								 @Value("${application-version}") String version){
		return new OpenAPI()
				.info(new Info()
						.title("FolksdevBank API")
						.version(version)
						.description(description)
						.license(new License().name("FolksdevBank API Licence")));
	}





	@Override
	public void run(String... args) throws Exception {

		Customer c1= Customer.builder()
				.city(City.ISTANBUL)
				.name("Emre Kırgız")
				.address(Address.builder().city(City.ISTANBUL).postCode("456312").addressDetails("Ev Adresi").build())
				.dateOfBirth(2005)
				.build();
		Customer c2= Customer.builder()
				.city(City.ANKARA)
				.name("Ali Kırgız")
				.address(Address.builder().city(City.MANISA).postCode("456312").addressDetails("İs Adresi").build())
				.dateOfBirth(1995)
				.build();
		Customer c3= Customer.builder()
				.city(City.CANKIRI)
				.name("Ayse Kırgız")
				.address(Address.builder().city(City.IZMIR).postCode("456312").addressDetails("Depo Adresi").build())
				.dateOfBirth(2005)
				.build();
		Customer c4= Customer.builder()
				.city(City.CANKIRI)
				.name("Fatma Kırgız")
				.address(Address.builder().city(City.CANKIRI).postCode("181818").addressDetails("Ciftlik Adresi").build())
				.dateOfBirth(2002)
				.build();

		customerRepository.saveAll(Arrays.asList(c1,c2,c3,c4));

		/*
		List<Address> addressList = new ArrayList<>();
		addressList.add(Address.builder()
				.postCode("34156")
				.customer(c1)
				.city(City.CANKIRI)
				.addressDetails("Ev Adresi")
				.build());

		addressList.add(Address.builder()
				.postCode("34155")
				.customer(c1)
				.city(City.KOCAELI)
				.addressDetails("İs Adresi")
				.build());

		c1.setAddress(addressList);
		customerRepository.save(c1);



		Address address1 = Address.builder()
				.postCode("181716")
				.customer(c2)
				.city(City.IZMIR)
				.addressDetails("Depo Adresi")
				.build();

		c2.setAddress(Arrays.asList(address1));
		customerRepository.save(c2);
		*/

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
