package com.gruppo4java11.MovieTips.repositories;

import com.gruppo4java11.MovieTips.entities.Favorite;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository of Favorites entity
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    /**
     * Custom query for searching and set active or deleted records from Favorites table records
     * @param recordStatus Logical status of the Favorites record
     * @param id id reference to database from Favorites table
     */
    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(value = "update Favorite SET recordStatus = :recordStatus WHERE id = :id")
    void updateStatusById(@Param(value = "recordStatus") RecordStatus recordStatus, @Param(value = "id") Long id);

    @Query(value = "select Max(id) from Favorite")
    Long getHighestID();
}
//:#{#role}
