package com.folksdevbank.folksdevbank.controller;

import com.folksdevbank.folksdevbank.dto.AccountDto;
import com.folksdevbank.folksdevbank.dto.request.CreateAccountRequest;
import com.folksdevbank.folksdevbank.dto.request.MoneyTransferRequest;
import com.folksdevbank.folksdevbank.dto.request.UpdateAccountRequest;
import com.folksdevbank.folksdevbank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateAccountRequest accountRequest){
        return ResponseEntity.ok(accountService.createAccount(accountRequest));
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable String id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable String id,
                                                    @Valid @RequestBody UpdateAccountRequest updateAccountRequest){
        return ResponseEntity.ok(accountService.updateAccount(id,updateAccountRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id){
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/withdraw/{id}/{amount}")
    public ResponseEntity<AccountDto> withdrawMoney(@PathVariable String  id,
                                                    @PathVariable BigDecimal amount){
        return ResponseEntity.ok(accountService.withdrawMoney(id,amount));
    }

    @PatchMapping("/add/{id}/{amount}")
    public ResponseEntity<AccountDto> addMoney(@PathVariable String  id,
                                                    @PathVariable BigDecimal amount){
        return ResponseEntity.ok(accountService.addMoney(id,amount));
    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody MoneyTransferRequest transferRequest){
        accountService.transferMoney(transferRequest);
        return ResponseEntity.ok("Your process taken successfully !");
    }

}
