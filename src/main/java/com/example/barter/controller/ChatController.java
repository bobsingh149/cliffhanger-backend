package com.example.barter.controller;


import com.example.barter.dto.input.ChatInput;
import com.example.barter.dto.model.ChatModel;
import com.example.barter.dto.response.ApiResponse;
import com.example.barter.service.ChatService;
import com.example.barter.utils.ControllerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;


@RestController
@Slf4j
public class ChatController {

    private final ChatService chatService;


    @Autowired
    public ChatController(final ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(value = "/api/chat/uploadImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> getImageLink(@RequestParam("file") MultipartFile file) throws IOException {
        return chatService.getImageLink(file);
    }


    @MessageMapping("/{id}")
    @SendTo("/topic/{id}")
    public ChatModel sendMessage(@PathVariable String id, @Payload ChatInput chatInput) {

        final ChatModel chatModel = ChatModel.builder().from(chatInput.from()).to(chatInput.to()).message(chatInput.message()).isRead(chatInput.isRead()).isEdited(chatInput.isEdited()).timestamp(LocalDateTime.now()).build();

        chatService.saveMessage(id,chatModel).subscribe(value -> {
        }, error -> {
            log.error(String.valueOf(error));
        });

        return chatModel;
    }

    @GetMapping("/api/loadMessages/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> loadMessages(@PathVariable String id) {

            final var response = chatService.loadMessages(id);

            return ControllerUtils.mapMonoToResponseEntity(response, ControllerUtils.ResponseMessage.success, HttpStatus.OK);
    }



}
