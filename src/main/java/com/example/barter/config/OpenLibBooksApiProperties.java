package com.example.barter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "openlibraryapi")
@ConfigurationPropertiesScan
@Data
public class OpenLibBooksApiProperties {

    private String url;
    private String coverImageUrl;
    private String baseurl;
    private String isbnEndpoint;
    private String bookshelfEndpoint;
    private String timeout;
    private Query query;

    @Data
    public static class Query
    {
        String limit;
        String fields;
        String jscmd;
        String format;
    }

}
