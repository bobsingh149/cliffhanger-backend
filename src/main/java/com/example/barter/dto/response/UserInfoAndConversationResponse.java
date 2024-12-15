package com.example.barter.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoAndConversationResponse {

    private final UserResponse userResponse;
    private final List<UserResponse> contacts;
}