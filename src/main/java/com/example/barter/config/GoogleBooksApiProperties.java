package com.example.barter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@ConfigurationProperties(prefix = "google-books-api")
@ConfigurationPropertiesScan
@Data
public class GoogleBooksApiProperties {

    private final String url;
    private final int timeout;
    private final Query query;

    @Data
   public static class Query
    {
        String key;
    }
}
