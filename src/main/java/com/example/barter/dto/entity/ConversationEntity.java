package com.example.barter.dto.entity;

import com.example.barter.dto.model.ChatModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Builder
@Table("conversation")
public class ConversationEntity {

    @Id
    private final String id;

    @Column("chats")
    private final ChatWrapper chatWrapper;

    @Builder
    public static record ChatWrapper(List<ChatModel> chats)
    {
    }
}
