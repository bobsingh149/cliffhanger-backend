package com.example.barter.dto.model;

import lombok.Builder;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Builder
public record ChatModel(UUID from, UUID to, String message, LocalDateTime timestamp, boolean isRead, boolean isEdited) {

}
