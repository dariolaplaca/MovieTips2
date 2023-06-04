package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.repositories.AccountRepository;
import com.gruppo4java11.MovieTips.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    public AccountController (AccountRepository accountRepository, AccountService accountService){
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        accountRepository.saveAndFlush(account);
        return ResponseEntity.ok("Account Created!");
    }

    @GetMapping
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Account> getAccount(@PathVariable long id) {
        return accountRepository.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable long id, @RequestBody Account account) {
        Account accountFromDB = accountRepository.findById(id).orElseThrow(()->new RuntimeException("Account not found"));
        accountFromDB.setName(account.getName());
        accountFromDB.setSurname(account.getSurname());
        accountFromDB.setBirthday(account.getBirthday());
        accountFromDB.setEmail(account.getEmail());
        accountFromDB.setPassword(account.getPassword());
        accountFromDB.setUserRole(account.getUserRole());
        accountRepository.saveAndFlush(accountFromDB);
        return ResponseEntity.ok("Account Updated!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccountById(@PathVariable long id) {
        accountRepository.deleteById(id);
        return ResponseEntity.ok("Deleted Account number: " + id);
    }

    @DeleteMapping("")
    public void deleteMultipleAccountsById(@PathVariable Iterable<? extends Long> ids) {
        accountRepository.deleteAllById(ids);
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllAccounts() {
        accountRepository.deleteAll();
        return ResponseEntity.ok("Deleted all the Accounts!");
    }

}
