package com.example.barter.dto.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.Doc;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BooksResponse {

    List<Docs> docs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Docs
    {
        List<String> isbn;
        String title;
        List<String> subject;

        @JsonProperty("author_name")
        List<String> authorNames;

        @JsonProperty("want_to_read_count")
        int wantCount;

        @JsonProperty("currently_reading_count")
        int readingCount;

        @JsonProperty("already_read_count")
        int alreadyReadCount;

    }
}



