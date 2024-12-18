package com.example.barter.dto.input;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public final class SaveConversationInput {
    private String conversationId;
    @JsonProperty("isGroup")
    private final boolean isGroup;
    private final List<String> members;
    private final String userId;
    private final String groupName;
    private String groupImage;

}


