package com.example.barter.dto.response;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class BookBuddyResponse {
    private final UserResponse userResponse;
    private final LocalDateTime timestamp;
    private final int commonSubjectCount;

} 