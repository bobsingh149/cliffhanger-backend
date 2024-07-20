package com.example.barter.controller;

import com.example.barter.dto.input.SaveConnectionInput;
import com.example.barter.dto.input.SaveRequestInput;
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
@RequestMapping("/api/user")
public class UserController {

   private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/saveUser")
    public ResponseEntity<Mono<ApiResponse<Object>>> save(@RequestBody SaveUserInput saveUserInput) {
        final var response = userService.saveUser(saveUserInput);
        return ControllerUtils.mapMonoToResponseEntity(response,  ResponseMessage.success, HttpStatus.CREATED);
    }


    @PostMapping("/saveConnection")
    public ResponseEntity<Mono<ApiResponse<Object>>> saveConnection(@RequestBody SaveConnectionInput saveConnectionInput) {

        final var response = userService.saveConnection(saveConnectionInput);
        return ControllerUtils.mapMonoToResponseEntity(response,ResponseMessage.success,HttpStatus.CREATED);
    }

    @GetMapping("/getConnections/{id}")
    public ResponseEntity<Flux<ApiResponse<Object>>> getConnections(@PathVariable UUID id) {

        final var response = userService.getConnections(id);

        return ControllerUtils.mapFLuxToResponseEntity(response,ResponseMessage.success,HttpStatus.OK);

    }

    @GetMapping("/getCommonUsers/{id}")
    public ResponseEntity<Flux<ApiResponse<Object>>> getCommonUsers(@PathVariable UUID id) {

        final var response = userService.getCommonUsers(id);

        return ControllerUtils.mapFLuxToResponseEntity(response,ResponseMessage.success,HttpStatus.OK);

    }


    @PostMapping("/saveRequest")
    public ResponseEntity<Mono<ApiResponse<Object>>> saveRequest(@RequestBody SaveRequestInput saveRequestInput) {

        final var response = userService.saveRequest(saveRequestInput);
        return ControllerUtils.mapMonoToResponseEntity(response,ResponseMessage.success,HttpStatus.CREATED);
    }

    @GetMapping("/getRequests/{id}")
    public ResponseEntity<Flux<ApiResponse<Object>>> getRequests(@PathVariable UUID id) {

        final var response = userService.getRequests(id);

        return ControllerUtils.mapFLuxToResponseEntity(response,ResponseMessage.success,HttpStatus.OK);

    }


    @GetMapping("/getUserById/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> getUser(@PathVariable UUID id) {
        final var response= userService.getUser(id);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success,HttpStatus.OK);

    }

    @PutMapping("/updateUser")
    public ResponseEntity<Mono<ApiResponse<Object>>> patchUser(@RequestBody SaveUserInput saveUserInput) {

            final var response = userService.updateUser(saveUserInput);

            return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success,HttpStatus.OK);
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> deleteUser(@PathVariable UUID id) {

        final var response = userService.deleteUser(id);

        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success,HttpStatus.OK);
    }
}
