package com.example.barter.dto.model;

import lombok.Builder;

@Builder
public record BookImage(String imageUrl, String caption) {
}