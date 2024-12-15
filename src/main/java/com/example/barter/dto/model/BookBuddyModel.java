package com.example.barter.dto.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class BookBuddyModel {
    private final String userId;
    private final LocalDateTime timestamp;
    private final int commonSubjectCount;
}
