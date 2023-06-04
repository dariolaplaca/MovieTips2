package com.gruppo4java11.MovieTips.repositories;

import com.gruppo4java11.MovieTips.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

}