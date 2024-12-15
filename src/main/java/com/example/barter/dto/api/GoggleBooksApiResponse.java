package com.example.barter.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoggleBooksApiResponse {

    List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
   public static class Item
    {
        VolumeInfo volumeInfo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
   public static class VolumeInfo
    {
        String title;

        List<String> authors;

        @JsonProperty("imageLinks")
        CoverImage coverImage;

        String description;

        @JsonProperty("categories")
        List<String> subjects;

        int ratingsCount;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
   public static class CoverImage
    {
        String smallThumbnail;

        String thumbnail;

    }

}
