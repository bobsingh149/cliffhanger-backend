package com.example.barter.dto.entity;


import com.example.barter.dto.api.BooksApiResponse;
import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.exception.customexception.InvalidIsbnException;
import com.example.barter.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Data
@Table(name = "product")
@Builder
public class ProductEntity {

    @Id
    private String id;

    @NonNull
    private long isbn;

    @NonNull
    @Column("userid")
    private UUID userId;

    private List<Map<String,String>>  works;

    @NonNull
    private String title;

    @NonNull
    private String[] authors;

    private String description;
    private String[] subjects;
    private String image;

    @NonNull
    private long score;

    @Column("createdat")
    private LocalDateTime createdAt;




    public static ProductEntity fromJson(String jsonStr, SaveProductInput saveProductInput)
    {
        var jsonObject = new JSONObject(jsonStr);

        var keys =  jsonObject.keys();

        List<BooksApiResponse> booksApiResponseList = new ArrayList<>();
        List<String> isbnList = new ArrayList<>() ;

        while (keys.hasNext())
        {
            final var key = keys.next();

            int idx = key.lastIndexOf(':');
            String isbn = key.substring(idx+1);

            isbnList.add(isbn);

            final JSONObject val = jsonObject.getJSONObject(key);


            try {
                booksApiResponseList.add(new ObjectMapper().readValue(val.toString(), BooksApiResponse.class));
            }
            catch (JsonProcessingException e)
            {
                throw  new InvalidIsbnException(e.getMessage());
            }

            catch (Exception e)
            {
                throw new RuntimeException("failed to get corresponding book info");
            }

        }


        if(booksApiResponseList.isEmpty())
        {
            throw  new InvalidIsbnException("Invalid isbn number");
        }

      final  BooksApiResponse booksApiResponse = booksApiResponseList.get(0);
       final String isbn =isbnList.get(0);


        return ProductEntity.builder().id(isbn + "_" + saveProductInput.getUserId().toString())
                .isbn(Long.parseLong(isbn))
                .image(booksApiResponse.getThumbnail_url())
                .score(0)
                .title(booksApiResponse.getDetails().getTitle())
                .authors(CommonUtils.toArray(booksApiResponse.getDetails().getAuthors().stream().map(author_map -> author_map.get("name"))))
                .description(booksApiResponse.getDetails().getDescription())
                .subjects(CommonUtils.toArray(booksApiResponse.getDetails().getSubjects()))
                .userId(saveProductInput.getUserId())
                .works(booksApiResponse.getDetails().getWorks())
                .build();

    }


}


