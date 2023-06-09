package com.gruppo4java11.MovieTips.repositories;

import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.entities.RentalOrder;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository of RentalOrder entity
 */
@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long> {
    /**
     * Custom query for searching and set active or deleted records from RentalOrder table records
     * @param recordStatus Logical status of the RentalOrder record
     * @param id id reference to database from RentalOrder table
     */
    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(value = "update RentalOrder SET recordStatus = :recordStatus WHERE id = :id")
    void updateStatusById(@Param(value = "recordStatus") RecordStatus recordStatus, @Param(value = "id") Long id);

    @Query(value = "select Max(id) from RentalOrder")
    Long getHighestID();

    @Query(value = "select ro from RentalOrder ro where ro.account.id = :account_id")
    List<RentalOrder> getRentalOrderListByAccountId(@Param(value = "account_id") Long accountId);

    @Query(value = "select ro from RentalOrder ro where ro.account.id = :account_id and ro.orderStatus = 'IN_PROGRESS'")
    List<RentalOrder> getRentalOrderListByAccountInProgress(@Param(value = "account_id") Long accountId);
}
