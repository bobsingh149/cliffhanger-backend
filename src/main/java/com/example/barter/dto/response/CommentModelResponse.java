package com.example.barter.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentModelResponse {
    private final String id;
    private final String text;
    private final UserResponse userResponse;
    private final LocalDateTime timestamp;
    private final int likeCount;
}
