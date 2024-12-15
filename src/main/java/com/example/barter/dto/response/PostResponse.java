package com.example.barter.dto.response;

import com.example.barter.dto.entity.ProductEntity;
import com.example.barter.dto.model.PostCategory;
import com.example.barter.dto.model.CommentModel;
import lombok.Builder;
import lombok.Data;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Builder
public final class PostResponse {
    private final UUID id;
    private final UserResponse userResponse;
    private final String title;
    private final String[] authors;
    private final String[]  subjects;
    private final String[] coverImages;
    private final long score;
    private final String postImage;
    private final String caption;
    private final String[] likes;
    private final int likeCount;
    private final int commentCount;
    private final PostCategory category;
    private final LocalDateTime createdAt;


    public static PostResponse fromProductEntity(ProductEntity productEntity, UserResponse userResponse)
    {
        return PostResponse.builder()
                .id(productEntity.getId())
                .userResponse(userResponse)
                .coverImages(productEntity.getCoverImages())
                .title(productEntity.getTitle())
                .authors(productEntity.getAuthors())
                .score(productEntity.getScore())
                .subjects(productEntity.getSubjects())
                .caption(productEntity.getCaption())
                .postImage(productEntity.getPostImage())
                .likes(productEntity.getLikes())
                .createdAt(productEntity.getCreatedAt())
                .category(productEntity.getCategory())
                .likeCount(productEntity.getLikes().length)
                .commentCount(productEntity.getCommentsWrapper().comments().size())
                .build();
    }
}

