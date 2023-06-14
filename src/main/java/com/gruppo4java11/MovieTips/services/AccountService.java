package com.gruppo4java11.MovieTips.services;

import com.gruppo4java11.MovieTips.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class that include all the Services and function of the Account entity
 */
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountService (AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
