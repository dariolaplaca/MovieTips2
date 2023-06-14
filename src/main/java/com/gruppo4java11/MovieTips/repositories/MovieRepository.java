package com.gruppo4java11.MovieTips.repositories;

import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

//TODO Fare filtri globali che ispezionino solo i record active (A)
/**
 * Repository of Movie entity
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {
    /**
     * Custom query for searching and set active or deleted records from Movie table records
     * @param recordStatus Logical status of the Movie recod
     * @param id id reference to database from Movie table
     */
    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(value = "update Movie SET recordStatus = :recordStatus WHERE id = :id")
    void updateStatusById(@Param(value = "recordStatus") RecordStatus recordStatus, @Param(value = "id") Long id);

}
