package com.gruppo4java11.MovieTips;

import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.repositories.MovieRepository;
import com.gruppo4java11.MovieTips.services.MovieService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MovieTipsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieTipsApplication.class, args);
	}

}
