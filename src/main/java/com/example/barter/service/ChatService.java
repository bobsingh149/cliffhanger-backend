package com.example.barter.service;

import com.example.barter.dto.entity.ConversationEntity;
import com.example.barter.dto.model.ChatModel;
import com.example.barter.repository.ConversationRepository;
import com.example.barter.utils.CloudinaryUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.*;

@Service
public class ChatService {

   private final ConversationRepository conversationRepository;
   private final CloudinaryUtils cloudinaryUtils;

    public ChatService(final ConversationRepository conversationRepository, final CloudinaryUtils cloudinaryUtils) {
        this.conversationRepository = conversationRepository;
        this.cloudinaryUtils=cloudinaryUtils;
    }

    public Mono<Void> saveMessage(String _id, ChatModel chatModel)  {
        final String separator = "_";

        int compareResult = chatModel.from().compareTo(chatModel.to());

        String id = compareResult < 0 ? (chatModel.from() + separator + chatModel.to()) : (chatModel.to() + separator + chatModel.from());

        if (!_id.equals(id)) {
            throw new IllegalArgumentException("id in the path variable is incorrect");
        }

      return conversationRepository.save(id,chatModel);

    }

    public Mono<List<ChatModel>> loadMessages(String id)
    {
        Mono<ConversationEntity> conversationEntityMono = conversationRepository.findById(id);

        return conversationEntityMono.map(conversationEntity -> conversationEntity.getChatWrapper().chats()
                .stream().sorted(Comparator.comparing(ChatModel::timestamp).reversed()).toList());

    }

    public String getImageLink(MultipartFile file) throws IOException {

        return cloudinaryUtils.uploadFileAndGetLink(file,"chat_images");

    }
}
