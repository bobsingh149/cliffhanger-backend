package com.example.barter.controller;


import com.example.barter.dto.input.ChatInput;
import com.example.barter.dto.model.ChatModel;
import com.example.barter.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(final ChatService chatService) {
        this.chatService = chatService;
    }



    @MessageMapping("/sendMessage/{id}")
    @SendTo("/topic/{id}")
    public ChatModel sendMessage(@Payload ChatInput chatInput) {

        final ChatModel chatModel = ChatModel.builder().from(chatInput.from()).to(chatInput.to()).message(chatInput.message()).isRead(chatInput.isRead()).isEdited(chatInput.isEdited()).timestamp(LocalDateTime.now()).build();

        chatService.saveMessage(chatModel).subscribe(value -> {
        }, error -> {
            log.error(String.valueOf(error));
        });

        return chatModel;
    }




}
