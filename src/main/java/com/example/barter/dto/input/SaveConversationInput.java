package com.example.barter.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
public final class SaveConversationInput {

    private String conversationId;
    private final boolean isGroup;
    private final List<String> users;
    private final String userId;
    private final String groupName;
    private final String groupImage;

}


