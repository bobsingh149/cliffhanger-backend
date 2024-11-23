package com.example.barter.dto.response;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class RequestResponse {
    private final UserResponse userResponse;
    private final LocalDateTime timestamp;
} 