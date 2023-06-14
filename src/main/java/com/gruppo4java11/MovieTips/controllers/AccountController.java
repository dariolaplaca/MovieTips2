package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.exception.AccountNotFoundException;
import com.gruppo4java11.MovieTips.repositories.AccountRepository;
import com.gruppo4java11.MovieTips.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller of the account entities
 */
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

    /**
     * This mapping is for the insertion of a new account in the database
     * @param account account to add in the database
     * @return Response Entity of String depending on the http status
     */
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
        if(accountRepository.findById(id).get().equals(null)) {
            throw new AccountNotFoundException("Account id not found" + id);
        }
        else{
        return accountRepository.findById(id);
        }
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

    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setAccountStatus(@PathVariable long id){
        Account accountToChange = accountRepository.findById(id).orElseThrow(()-> new RuntimeException("Account not found!"));
        if(accountToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) accountToChange.setRecordStatus(RecordStatus.DELETED);
        else accountToChange.setRecordStatus(RecordStatus.ACTIVE);
        accountRepository.updateStatusById(accountToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Account with id " + id + " Status Updated to " + accountToChange.getRecordStatus());
    }

}
