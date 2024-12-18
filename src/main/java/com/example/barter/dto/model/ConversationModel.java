package com.example.barter.dto.model;


import com.example.barter.dto.input.SaveConversationInput;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConversationModel
{
    private final String conversationId;
    @JsonProperty("isGroup")
    private final boolean isGroup;
    private final List<String> members;
    private String userId;
    private final String groupName;
    private final String groupImage;


    public static ConversationModel fromSaveConversationInput(SaveConversationInput saveConversationInput)
    {
        return ConversationModel.builder()
                .conversationId(saveConversationInput.getConversationId())
                .isGroup(saveConversationInput.isGroup())
                .members(saveConversationInput.getMembers())
                .userId(saveConversationInput.getUserId())
                .groupName(saveConversationInput.getGroupName())
                .groupImage(saveConversationInput.getGroupImage())
                .build();
    }

}



