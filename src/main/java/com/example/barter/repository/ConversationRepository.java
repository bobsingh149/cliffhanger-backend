package com.example.barter.repository;

import com.example.barter.dto.entity.ConversationEntity;
import com.example.barter.dto.model.ChatModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository
public interface ConversationRepository extends R2dbcRepository<ConversationEntity,String> {

    @Query("""
               INSERT INTO conversation (id, chats) VALUES (:id, :chat)
               ON CONFLICT (id)
               DO UPDATE SET chats = chats || :chat
            """ )
    Mono<Void> save(String id, ChatModel chat);
}
