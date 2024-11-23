package com.example.barter.dto.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentModel {

    private final String text;
    private final String userId;
    private final LocalDateTime timestamp;
    private final int likeCount;
}
