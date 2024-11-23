package com.example.barter.dto.input;

import com.example.barter.dto.model.CommentModel;

public record CommentInput(
    String productId,
    CommentModel comment
) {}

