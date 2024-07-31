package com.example.barter.dto.entity;

import com.example.barter.dto.model.ChatModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;

@Data
@Builder
@Table("conversation")
public class ConversationEntity {

    @Id
    private final String id;

    private final ChatModel[] chats;
}
