package com.example.barter.dto.input;

import java.util.UUID;

public record CommentInput(
    UUID productId,
    String text,
    String userId
) {}

