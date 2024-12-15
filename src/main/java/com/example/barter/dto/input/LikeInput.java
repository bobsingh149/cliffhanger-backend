package com.example.barter.dto.input;


import java.util.UUID;

public record LikeInput(
    UUID productId,
    String userId
) {}

