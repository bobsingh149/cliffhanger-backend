package com.example.barter.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ConversationResponse {
    private final String conversationId;
    @JsonProperty("isGroup")
    private final boolean isGroup;
    private final List<UserResponse> members;
    private final UserResponse userResponse;
    private final String groupName;
    private final String groupImage;
} 