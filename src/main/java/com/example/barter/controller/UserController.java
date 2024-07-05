//package com.example.barter.controller;
//
//
//import com.example.barter.dto.input.SaveUserInput;
//import com.example.barter.dto.response.ApiResponse;
//import com.example.barter.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1")
//public class UserController {
//
//    final UserService userService;
//
//    @Autowired
//   public UserController(UserService userService)
//    {
//        this.userService = userService;
//    }
//
//
//    @PostMapping("/user/save")
//    public ResponseEntity<ApiResponse<Object>> save(@RequestBody SaveUserInput saveUserInput)
//    {
//
//    }
//
//    @GetMapping("/user/getById/{id}")
//    public ResponseEntity<ApiResponse<Object>> get(@PathVariable UUID id)
//    {
//
//    }
//
//
//    @PatchMapping("/user/updateById/{id}")
//    public ResponseEntity<ApiResponse<Object>> patch(@PathVariable UUID id)
//    {
//
//    }
//
//
//    @DeleteMapping("/user/deleteById/{id}")
//    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable UUID id)
//    {
//
//    }
//}
