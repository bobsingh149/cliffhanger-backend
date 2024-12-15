package com.example.barter.dto.response;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class RequestResponse implements Comparable<RequestResponse> {
    private final UserResponse userResponse;
    private final LocalDateTime timestamp;

    @Override
    public int compareTo(RequestResponse other) {
        return other.timestamp.compareTo(this.timestamp); // Reverse order (newest first)
    }
} 