package com.example.barter.dto.entity;


import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.model.CommentModel;
import com.example.barter.dto.model.PostCategory;
import com.example.barter.utils.CloudinaryUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Table(name = "product")
@Builder
public class ProductEntity {

    @Id
    private final UUID id;

    @NonNull
    @Column("userid")
    private final String userId;


    @NonNull
    private final String title;

    @NonNull
    private final String[] authors;

    private final String[] subjects;

    @Column("cover_images")
    private String[] coverImages;

    @NonNull
    private  long score;


    private final String caption;

    @Column("post_image")
    private final String postImage;

    private final PostCategory category;


    private final String[] likes;


    @Column("comments")
    private final CommentsWrapper commentsWrapper;

    @Column("created_at")
    private final LocalDateTime createdAt;

    @Builder
    public static record CommentsWrapper (List<CommentModel> comments){}


    public void setCoverImage(String image)
    {
        if(coverImages.length>=3) {
            coverImages[2] = image;
        }
    }


    public static ProductEntity fromProductInput(SaveProductInput saveProductInput, MultipartFile file, CloudinaryUtils cloudinaryUtils) throws IOException {

        final String imageLink = file != null ? cloudinaryUtils.uploadFileAndGetLink(file, "post_images") : null;

        return ProductEntity.builder()
                .userId(saveProductInput.userId())
                .title(saveProductInput.title())
                .caption(saveProductInput.caption())
                .authors(saveProductInput.authors().toArray(new String[0]))
                .coverImages(saveProductInput.coverImages().toArray(new String[0]))
                .score(saveProductInput.score())
                .subjects(saveProductInput.subjects().toArray(new String[0]))
                .postImage(imageLink)
                .category(saveProductInput.category())
                .build();
    }


}


