package com.example.barter.dto.response;


import com.example.barter.dto.api.BooksResponse;
import com.example.barter.exception.customexception.InvalidIsbnException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Stream;

@Data
@Builder
@AllArgsConstructor
public final class ProductResponse {



    private final String title;
    private final List<String> authors;
    private final List<String> subjects;
    private final List<String> coverImages;
    private final long score;

    public static List<String> getImages(List<String> isbnList) {

        if(isbnList==null || isbnList.isEmpty())
            return List.of("","","");

        String isbn = isbnList.get(0);

        String coverImageUrl = "https://covers.openlibrary.org/b/isbn";

        return Stream.of("S", "M", "L").map(size -> String.format("%s/%s-%s.jpg", coverImageUrl, isbn, size)).toList();
    }


    public static List<ProductResponse> fromJson(String jsonStr) {

        try {
            BooksResponse booksResponseList = new ObjectMapper().readValue(jsonStr, BooksResponse.class);
            return booksResponseList.getDocs().stream().map((BooksResponse.Docs doc) ->
                            ProductResponse.builder()
                                    .title(doc.getTitle())
                    .coverImages(getImages(doc.getIsbn()))
                    .authors(doc.getAuthorNames())
                    .subjects(doc.getSubject())
                    .score(doc.getAlreadyReadCount() * 35L + doc.getReadingCount() * 50L + doc.getWantCount() * 25L)
                    .build())
                    .toList();

        } catch (JsonProcessingException e) {
            throw new InvalidIsbnException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("failed to get corresponding book info");

        }

    }
}

