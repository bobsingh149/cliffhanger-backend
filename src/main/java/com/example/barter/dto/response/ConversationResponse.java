package com.example.barter.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ConversationResponse {
    private final String conversationId;
    private final boolean isGroup;
    private final List<UserResponse> users;
    private final UserResponse userResponse;
    private final String groupName;
    private final String groupImage;
} 