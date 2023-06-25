package com.gruppo4java11.MovieTips.controllers;

import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import com.gruppo4java11.MovieTips.exception.AccountNotFoundException;
import com.gruppo4java11.MovieTips.repositories.AccountRepository;
import com.gruppo4java11.MovieTips.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller of the account entities
 */
@RestController
@RequestMapping("/api/account")
@Tag(name = "Accounts API")
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
    @Operation(summary = "Create a new Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account Successfully created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)) })
    })
    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@Parameter(description = "Body of the account to create") @RequestBody Account account, @Parameter(description = "Name of the user whom is creating the account") @RequestParam String username) {
        account.setCreatedOn(LocalDateTime.now());
        account.setCreatedBy(username);
        account.setModifiedBy(username);
        account.setModifiedOn(LocalDateTime.now());
        accountRepository.saveAndFlush(account);
        Long highestId = accountRepository.getHighestID();
        return ResponseEntity.ok("Account Created with id " + highestId);
    }
    /**
     * This mapping retrieves all the accounts from the database
     * @return List of accounts retrieved from the database
     */
    @GetMapping("/all")
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }
    /**
     * This mapping retrieves the account with the specified ID from the database
     * @param id  ID of the account to retrieve
     * @return Optional containing the account with the specified ID
     */
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable long id) {
        if(accountRepository.findById(id).isEmpty()) {
            throw new AccountNotFoundException("Account id not found" + id);
        }
        return accountRepository.findById(id).get();

    }
    /**
     * This mapping is for updates the account with the specified ID in the database
     * @param id ID of the account to update
     * @param account the updated account details
     * @return ResponseEntity indicating the success of the account update
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable long id, @RequestBody Account account, @RequestParam String username) {
        Account accountFromDB = accountRepository.findById(id).orElseThrow(()->new RuntimeException("Account not found"));
        accountFromDB.setName(account.getName());
        accountFromDB.setSurname(account.getSurname());
        accountFromDB.setBirthday(account.getBirthday());
        accountFromDB.setEmail(account.getEmail());
        accountFromDB.setPassword(account.getPassword());
        accountFromDB.setUserRole(account.getUserRole());
        accountFromDB.setModifiedBy(username);
        accountFromDB.setModifiedOn(LocalDateTime.now());
        accountRepository.saveAndFlush(accountFromDB);
        return ResponseEntity.ok("Account Updated!");
    }
    /**
     * Deletes the account with the specified ID from the database
     * @param id Deletes the account with the specified ID from the database
     * @return ResponseEntity indicating the success of the account deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccountById(@PathVariable long id) {
        accountRepository.deleteById(id);
        return ResponseEntity.ok("Deleted Account number: " + id);
    }
    /**
     * Deletes multiple accounts with the specified IDs from the database
     * @param ids IDs of the accounts to delete
     */
    @DeleteMapping("")
    public void deleteMultipleAccountsById(@PathVariable Iterable<? extends Long> ids) {
        accountRepository.deleteAllById(ids);
    }
    /**
     * Deletes all the accounts from the database
     * @return
     */
    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllAccounts() {
        accountRepository.deleteAll();
        return ResponseEntity.ok("Deleted all the Accounts!");
    }

    /**
     * Sets the status of an account identified by the given ID
     * @param id ID of the account to update
     * @return  ResponseEntity containing a message indicating the updated status of the account
     */
    @PatchMapping("/set-status/{id}")
    public ResponseEntity<String> setAccountStatus(@PathVariable long id, @RequestParam String username){
        Account accountToChange = accountRepository.findById(id).orElseThrow(()-> new RuntimeException("Account not found!"));
        accountToChange.setModifiedOn(LocalDateTime.now());
        accountToChange.setModifiedBy(username);
        if(accountToChange.getRecordStatus().equals(RecordStatus.ACTIVE)) accountToChange.setRecordStatus(RecordStatus.DELETED);
        else accountToChange.setRecordStatus(RecordStatus.ACTIVE);
        accountRepository.updateStatusById(accountToChange.getRecordStatus(), id);
        return ResponseEntity.ok("Account with id " + id + " Status Updated to " + accountToChange.getRecordStatus());
    }

}
