package com.example.barter.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BooksApiResponse {

     String thumbnail_url;
     Details details;

     @Data
     @NoArgsConstructor
     @AllArgsConstructor
     @Builder
     @JsonIgnoreProperties(ignoreUnknown = true)
     public static class Details
     {
          String title;
          List<String> subjects;
          List<Map<String,String>> authors;
          String description;
          List<Map<String,String>> works;

     }
}



