package com.example.barter.controller;

import com.example.barter.utils.ControllerUtils.ResponseMessage;
import com.example.barter.dto.input.SaveUserInput;
import com.example.barter.dto.response.ApiResponse;
import com.example.barter.service.UserService;
import com.example.barter.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/user/save")
    public ResponseEntity<Mono<ApiResponse<Object>>> save(@RequestBody SaveUserInput saveUserInput) {
        final var response = userService.save(saveUserInput);
        return ControllerUtils.mapMonoToResponseEntitiy(response,  ResponseMessage.success, HttpStatus.CREATED);
    }

    @GetMapping("/user/getById/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> get(@PathVariable String id) {
        final var response= userService.get(id);
        return ControllerUtils.mapMonoToResponseEntitiy(response, ResponseMessage.success,HttpStatus.OK);

    }


    @PutMapping("/user/updateById")
    public ResponseEntity<Flux<ApiResponse<Object>>> patch(@RequestBody SaveUserInput saveUserInput) {

            final var response = userService.update(saveUserInput);

            return ControllerUtils.mapFLuxToResponseEntitiy(response, ResponseMessage.success,HttpStatus.OK);
    }


    @DeleteMapping("/user/deleteById/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> delete(@PathVariable String id) {

        final var response = userService.delete(id);

        return ControllerUtils.mapMonoToResponseEntitiy(response, ResponseMessage.success,HttpStatus.OK);
    }
}
