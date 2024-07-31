package com.example.barter.service;

import com.example.barter.dto.entity.ConversationEntity;
import com.example.barter.dto.model.ChatModel;
import com.example.barter.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatService {

    final ConversationRepository conversationRepository;

    public ChatService(final ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Mono<Void> saveMessage(String _id, ChatModel chatModel) {

        int compareResult = chatModel.from().compareTo(chatModel.to());

        String id = compareResult < 0 ? chatModel.from().toString() + chatModel.to() : chatModel.to().toString() + chatModel.from();

        if (!_id.equals(id)) {
            throw new IllegalArgumentException("id in the path variable is incorrect");
        }

        return conversationRepository.save(id, chatModel);
    }

    public Mono<List<ChatModel>> loadMessages(String id)
    {
        Mono<ConversationEntity> conversationEntityMono = conversationRepository.findById(id);


     return  conversationEntityMono.map(conversationEntity -> {
                Arrays.stream(conversationEntity.getChats())
                    .sorted(Comparator.comparing(ChatModel::timestamp).reversed()).toList();
        });
    }
}
