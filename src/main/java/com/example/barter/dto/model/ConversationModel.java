package com.example.barter.dto.model;


import com.example.barter.dto.input.SaveConversationInput;
import com.example.barter.dto.response.UserResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConversationModel
{
    private final String conversationId;
    private final boolean isGroup;
    private final List<String> users;
    private final String userId;
    private final String groupName;
    private final String groupImage;


    public static ConversationModel fromSaveConversationInput(SaveConversationInput saveConversationInput)
    {
        return ConversationModel.builder()
                .conversationId(saveConversationInput.getConversationId())
                .isGroup(saveConversationInput.isGroup())
                .users(saveConversationInput.getUsers())
                .userId(saveConversationInput.getUserId())
                .groupName(saveConversationInput.getGroupName())
                .groupImage(saveConversationInput.getGroupImage())
                .build();
    }

}



