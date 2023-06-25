package com.gruppo4java11.MovieTips.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiDocumentationConfig {

    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI().info(new Info()
                .title("MovieTips API")
                .description("API for managing movie rentals and recommendations")
                .version("0.0.1")
                .contact(new Contact()
                        .name("Java 11 Gruppo 4")
                        .url("https://www.develhope.co/"))
        ).externalDocs(new ExternalDocumentation()
                .description("MovieTips Github")
                .url("https://github.com/dariolaplaca/MovieTips2")
        );
    }
}
