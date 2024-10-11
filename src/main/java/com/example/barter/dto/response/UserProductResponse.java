package com.example.barter.dto.response;

import com.example.barter.dto.entity.ProductEntity;
import com.example.barter.dto.model.PostCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;




@Data
@Builder
public final class UserProductResponse {
    private final String id;
    private final String userId;
    private final String title;
    private final String[] authors;
    private final String[]  subjects;
    private final String[] coverImages;
    private final long score;
    private final LocalDateTime createdAt;
    private final String postImage;
    private final String caption;
    private final PostCategory category;


    public static UserProductResponse fromProductEntity(ProductEntity productEntity)
    {
        return UserProductResponse.builder()
                .id(productEntity.getId())
                .userId(productEntity.getUserId())
                .coverImages(productEntity.getCoverImages())
                .title(productEntity.getTitle())
                .authors(productEntity.getAuthors())
                .score(productEntity.getScore())
                .subjects(productEntity.getSubjects())
                .caption(productEntity.getCaption())
                .postImage(productEntity.getPostImage())
                .createdAt(productEntity.getCreatedAt())
                .category(productEntity.getCategory())
                .build();
    }
}

