package com.gruppo4java11.MovieTips.repositories;

import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.enumerators.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

//TODO Fare filtri globali che ispezionino solo i record active (A)
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(value = "update Movie SET recordStatus = :recordStatus WHERE id = :id")
    void updateStatusById(@Param(value = "recordStatus") RecordStatus recordStatus, @Param(value = "id") Long id);

}
