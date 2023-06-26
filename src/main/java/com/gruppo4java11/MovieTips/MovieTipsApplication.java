package com.gruppo4java11.MovieTips;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MovieTipsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieTipsApplication.class, args);
	}

}
