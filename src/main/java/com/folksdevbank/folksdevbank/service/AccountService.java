package com.folksdevbank.folksdevbank.service;

import com.folksdevbank.folksdevbank.dto.AccountDto;
import com.folksdevbank.folksdevbank.dto.request.CreateAccountRequest;
import com.folksdevbank.folksdevbank.dto.converter.AccountDtoConverter;
import com.folksdevbank.folksdevbank.dto.request.MoneyTransferRequest;
import com.folksdevbank.folksdevbank.dto.request.UpdateAccountRequest;
import com.folksdevbank.folksdevbank.exception.AccountNotFoundException;
import com.folksdevbank.folksdevbank.model.Account;
import com.folksdevbank.folksdevbank.model.Customer;
import com.folksdevbank.folksdevbank.repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class  AccountService {
    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final AccountDtoConverter converter;
    private static final Logger logger= LoggerFactory.getLogger(AccountService.class);
    private final AmqpTemplate rabbitTemplate;

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final DirectExchange exchange;

    @Value("${sample.rabbitmq.routingKey}")
    String routingKey;
    @Value(("${sample.rabbitmq.queue}"))
    String queueName;


    public AccountService(AccountRepository accountRepository, CustomerService customerService, AccountDtoConverter converter, AmqpTemplate rabbitTemplate, KafkaTemplate<String, String> kafkaTemplate, DirectExchange exchange) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
        this.converter = converter;
        this.rabbitTemplate = rabbitTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.exchange = exchange;
    }

    public AccountDto createAccount(CreateAccountRequest accountRequest){
        Customer customer = customerService.findCustomerById(accountRequest.getCustomerId());

        Account account = Account.builder()
                .id(accountRequest.getId())
                .customerId(accountRequest.getCustomerId())
                .city(accountRequest.getCity())
                .balance(accountRequest.getBalance())
                .currency(accountRequest.getCurrency()).build();

        return converter.convertToAccountDto(accountRepository.save(account));
    }

    private Account findAccountById(String id){
        return accountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException("Account could not found by id " + id));
    }

    public AccountDto updateAccount(String id,UpdateAccountRequest accountRequest){
        Customer customer = customerService.findCustomerById(accountRequest.getCustomerId());

        Account account = findAccountById(id);
        account.setCustomerId(accountRequest.getCustomerId());
        account.setBalance(accountRequest.getBalance());
        account.setCity(accountRequest.getCity());
        account.setCurrency(accountRequest.getCurrency());

        return converter.convertToAccountDto(accountRepository.save(account));
    }

    public List<AccountDto> getAllAccounts(){
        return accountRepository.findAll()
                .stream()
                .map(converter::convertToAccountDto)
                .collect(Collectors.toList());
    }

    public AccountDto getAccountById(String id){
        return converter.convertToAccountDto(findAccountById(id));
    }

    public void deleteAccount(String id){
        accountRepository.deleteById(id);
    }

    public AccountDto withdrawMoney(String id, BigDecimal amount){
        Account account = findAccountById(id);

        if(account.getBalance().compareTo(amount) == 1 ) {
            account.setBalance(account.getBalance().subtract(amount) );

        }else{
                System.out.println("Insufficient funds -> accountId" + id
                    + " balance :  " +account.getBalance()
                    + "amount : " + amount
            );
        }

        return converter.convertToAccountDto(accountRepository.save(account));
    }

    public AccountDto addMoney(String id, BigDecimal amount){
        Account account = findAccountById(id);
        account.setBalance(account.getBalance().add(amount) );
        return converter.convertToAccountDto(accountRepository.save(account));
    }

    public void transferMoney(MoneyTransferRequest transferRequest) {
        rabbitTemplate.convertAndSend(exchange.getName(),routingKey,transferRequest);
    }

    @RabbitListener(queues = "${sample.rabbitmq.queue}")
    public void transferMoneyMessages(MoneyTransferRequest transferRequest){
        Optional<Account> accountOptional=accountRepository.findById(transferRequest.getFromId());
        accountOptional.ifPresentOrElse(account -> {
                    if(account.getBalance().compareTo(transferRequest.getAmount()) == 1 ){
                        account.setBalance(account.getBalance().subtract(transferRequest.getAmount()));
                        accountRepository.save(account);
                        rabbitTemplate.convertAndSend(exchange.getName(),"secondRoutingKey",transferRequest);
                    }else{
                        System.out.println("Insufficient funds -> accountId" + transferRequest.getFromId()
                                + " balance :  " +account.getBalance()
                                + "amount : " + transferRequest.getAmount()
                        );
                    }},
                ()-> System.out.println("Account not found")
            );
    }

    @RabbitListener(queues = "secondStepQueue")
    public void updateReceiverAccount(MoneyTransferRequest transferRequest){
        Optional<Account> accountOptional=accountRepository.findById(transferRequest.getToId());
        accountOptional.ifPresentOrElse( account -> {
                        account.setBalance(account.getBalance().add(transferRequest.getAmount()));
                        accountRepository.save(account);
                        rabbitTemplate.convertAndSend(exchange.getName(),"thirdRoutingKey",transferRequest);
                        },
                () -> {
                    System.out.println("Receiver Account not found");
                    Optional<Account> senderAccount=accountRepository.findById(transferRequest.getFromId());
                    senderAccount.ifPresent( sender -> {
                        System.out.println("Money charge back to sender");
                        sender.setBalance(sender.getBalance().add(transferRequest.getAmount()));
                        accountRepository.save(sender);
                    });
                }
        );
    }

    @RabbitListener(queues = "thirdStepQueue")
    public void finalizeTransfer(MoneyTransferRequest transferRequest){
        Optional<Account> senderAccountOptional = accountRepository.findById(transferRequest.getFromId());
        senderAccountOptional.ifPresentOrElse(sender -> {
            String notificationMessage = "Dear customer %s \n Your money transfer request has been succeed. Your new balance is %s";
            System.out.println("Sender("+sender.getId()+") new account balance: "+ sender.getBalance());
            String senderMessage = String.format(notificationMessage,sender.getId(),sender.getBalance());
            kafkaTemplate.send("transfer-notification",senderMessage);
            },
                ()-> System.out.println("Account not found")
        );

        Optional<Account> receiverAccountOptional=accountRepository.findById(transferRequest.getToId());
        receiverAccountOptional.ifPresentOrElse(receiver -> {
            String notificationMessage = "Dear customer %s \n You received a money transfer from %s. Your new balance is %s";
            System.out.println("Receiver("+receiver.getId()+") new account balance: "+receiver.getBalance());
            String receiverMessage = String.format(notificationMessage,receiver.getId(),transferRequest.getFromId(),receiver.getBalance());
            kafkaTemplate.send("transfer-notification",receiverMessage);
            },
                () -> System.out.println("Account not found")
        );
    }

}
