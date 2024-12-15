package com.example.barter.controller;

import com.example.barter.dto.input.SaveConversationInput;
import com.example.barter.dto.input.SaveGroupInput;
import com.example.barter.dto.response.ApiResponse;
import com.example.barter.service.GroupService;
import com.example.barter.utils.ControllerUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;
    private final ObjectMapper objectMapper;

    @Autowired
    public GroupController(GroupService groupService, ObjectMapper objectMapper)
    {
        this.groupService=groupService;
        this.objectMapper=objectMapper;
    }

    @PostMapping(value = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mono<ApiResponse<Object>>> saveGroup(@RequestParam(value = "file",required = false) MultipartFile iconFile, @RequestParam("data") String groupData) throws IOException {
        final SaveGroupInput saveGroupInput = objectMapper.readValue(groupData,SaveGroupInput.class);

        final var response = groupService.saveGroup(saveGroupInput,iconFile);

        return ControllerUtils.mapMonoToResponseEntity(response, ControllerUtils.ResponseMessage.success, HttpStatus.CREATED);
    }

}
