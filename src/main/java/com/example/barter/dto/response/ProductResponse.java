package com.example.barter.dto.response;

import com.example.barter.dto.entity.ProductEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;




@Data
@Builder
public final class ProductResponse {
    private final String id;
    private final long isbn;
    private UUID userId;
    private final String title;
    private final String description;
    private final List<Map<String,String>> authors;
    private final  List<String>  subjects;
    private final String image;
    private final long score;
    private final LocalDateTime createdAt;


    public static ProductResponse fromProductEntity(ProductEntity productEntity)
    {
        return ProductResponse.builder()
                .id(productEntity.getId())
                .isbn(productEntity.getIsbn())
                .userId(productEntity.getUserId())
                .image(productEntity.getImage())
                .title(productEntity.getTitle())
                .description(productEntity.getDescription())
                .authors(productEntity.getAuthors())
                .score(productEntity.getScore())
                .subjects(productEntity.getSubjects())
                .createdAt(productEntity.getCreatedAt())
                .build();
    }
}

