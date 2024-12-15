package com.example.barter.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.barter.dto.input.SaveConversationInput;
import com.example.barter.dto.input.SaveRequestInput;
import com.example.barter.dto.input.SaveUserInput;
import com.example.barter.dto.response.ApiResponse;
import com.example.barter.dto.response.UserResponse;
import com.example.barter.service.UserService;
import com.example.barter.utils.ControllerUtils;
import com.example.barter.utils.ControllerUtils.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
public class UserController {

   private final UserService userService;
   private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper=objectMapper;
    }

    @PostMapping(value = "/saveUser",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mono<ApiResponse<Object>>> save(@RequestParam(value = "file", required = false) MultipartFile file,
                                                          @RequestParam("data") String data) throws IOException {
        final SaveUserInput saveUserInput = objectMapper.readValue(data, SaveUserInput.class);
        final var response = userService.saveUser(saveUserInput,file);
        return ControllerUtils.mapMonoToResponseEntity(response,  ResponseMessage.success, HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateUser",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mono<ApiResponse<Object>>> patchUser(@RequestParam(value = "file", required = false) MultipartFile file,
                                                               @RequestParam("data") String data) throws IOException {

        final SaveUserInput saveUserInput = objectMapper.readValue(data, SaveUserInput.class);
        final var response = userService.updateUser(saveUserInput,file);
        return ControllerUtils.mapMonoToResponseEntity(response,  ResponseMessage.success, HttpStatus.OK);
    }

    @PostMapping("/saveConnection/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> saveConnection( @PathVariable String id,@RequestBody SaveConversationInput saveConversationInput) {

        final var response = userService.saveConnection(id,saveConversationInput);
        return ControllerUtils.mapMonoToResponseEntity(response,ResponseMessage.success,HttpStatus.CREATED);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> getUser(@PathVariable String id) {
        final var response= userService.getUser(id);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success,HttpStatus.OK);

    }

    @GetMapping("/getUserSetup/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> getUserSetup(@PathVariable String id) {

        final var response = userService.getUserSetup(id);

        return ControllerUtils.mapMonoToResponseEntity(response,ResponseMessage.success,HttpStatus.OK);

    }

    @GetMapping("/getCommonUsers/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> getCommonUsers(@PathVariable String id, @RequestParam int page, @RequestParam int size) {

        final var response = userService.getCommonUsers(id, PageRequest.of(page,size));

        return ControllerUtils.mapFLuxToResponseEntity(response,ResponseMessage.success,HttpStatus.OK);

    }

    @GetMapping("/getBookBuddy")
    public ResponseEntity<Mono<ApiResponse<Object>>> getBookBuddy(@RequestParam String id)
    {
        final var response = userService.getBookBuddy(id);
        return ControllerUtils.mapFLuxToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }


    @PostMapping("/saveRequest")
    public ResponseEntity<Mono<ApiResponse<Object>>> saveRequest(@RequestBody SaveRequestInput saveRequestInput) {

        final var response = userService.saveRequest(saveRequestInput);
        return ControllerUtils.mapMonoToResponseEntity(response,ResponseMessage.success,HttpStatus.CREATED);
    }

    @PatchMapping("/removeRequest")
    public ResponseEntity<Mono<ApiResponse<Object>>> removeRequest(@RequestBody SaveRequestInput saveRequestInput) {

        final var response = userService.removeRequest(saveRequestInput);
        return ControllerUtils.mapMonoToResponseEntity(response,ResponseMessage.success,HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> deleteUser(@PathVariable String id) {

        final var response = userService.deleteUser(id);

        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success,HttpStatus.OK);
    }

   

}
