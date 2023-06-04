package com.gruppo4java11.MovieTips.repositories;

import com.gruppo4java11.MovieTips.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
