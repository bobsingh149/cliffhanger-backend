package com.example.barter.dto.input;

import lombok.Data;

import java.util.List;


@Data
public final class SaveConversationInput {
    private String conversationId;
    private final boolean isGroup;
    private final List<String> members;
    private final String userId;
    private final String groupName;
    private final String groupImage;

}


