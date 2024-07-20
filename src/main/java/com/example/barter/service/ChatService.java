package com.example.barter.service;

import com.example.barter.dto.model.ChatModel;
import com.example.barter.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChatService {

    final ConversationRepository conversationRepository;

    public ChatService(final ConversationRepository conversationRepository)
    {
            this.conversationRepository = conversationRepository;
    }
    public Mono<Void> saveMessage(ChatModel chatModel)
    {

        int compareResult = chatModel.from().compareTo(chatModel.to());

        String id = compareResult<0 ? chatModel.from().toString() + chatModel.to().toString() : chatModel.to().toString() + chatModel.from().toString();

        return conversationRepository.save(id,chatModel.convertToMap());
    }
}
