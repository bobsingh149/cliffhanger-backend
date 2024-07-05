package com.example.barter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient getWebClient()
    {
        final BooksApiProperties booksApiProperties = new BooksApiProperties();
        return WebClient.builder().build();
    }


}
