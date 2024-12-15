package com.example.barter.dto.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class RequestModel {
    private final String userId;
    private final LocalDateTime timestamp;
} 