package com.example.barter.dto.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CommentModel {
    private final String id;
    private final String text;
    private final String userId;
    private final LocalDateTime timestamp;
    private final int likeCount;
}
