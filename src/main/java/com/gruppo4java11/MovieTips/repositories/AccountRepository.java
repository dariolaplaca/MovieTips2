package com.gruppo4java11.MovieTips.repositories;

import com.gruppo4java11.MovieTips.entities.Account;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository of account entity
 */
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    /**
     * Custom query for searching and set active or deleted records from account table records
     * @param recordStatus Logical status of the user record
     * @param id id reference to database from Account table
     */
    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(value = "update Account SET recordStatus = :recordStatus WHERE id = :id")
    void updateStatusById(@Param(value = "recordStatus") RecordStatus recordStatus, @Param(value = "id") Long id);

}