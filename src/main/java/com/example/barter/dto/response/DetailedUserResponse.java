package com.example.barter.dto.response;

import com.example.barter.dto.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DetailedUserResponse {
    private final String id;
    private final String name;
    private final int age;
    private final String bio;
    private final String city;
    private final String profileImage;
    private final String[] connections;
    private final List<BookBuddyResponse> bookBuddies;
    private final List<ConversationResponse> conversations;
    private final List<RequestResponse> requests;


    public static DetailedUserResponse fromUserEntity(UserEntity userEntity, Map<String, UserEntity> userMap) {
        return DetailedUserResponse.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .age(userEntity.getAge())
                .profileImage(userEntity.getProfileImage())
                .connections(userEntity.getConnections())
                .bio(userEntity.getBio())
                .city(userEntity.getCity())
                .conversations(userEntity.getConversationWrapper().conversations().stream().map(conversation->ConversationResponse.builder()
                        .isGroup(conversation.isGroup())
                        .groupImage(conversation.getGroupImage())
                        .conversationId(conversation.getConversationId())
                        .groupName(conversation.getGroupName())
                        .userResponse(UserResponse.fromUserEntity(userMap.get(conversation.getUserId())))
                        .users(conversation.getUsers().stream().map(id->UserResponse.fromUserEntity(userMap.get(id))).toList()).build()
                ).toList())
                .bookBuddies(userEntity.getBookBuddyWrapper().bookBuddies().stream()
                        .map(bookBuddy->BookBuddyResponse.builder().userResponse(UserResponse.fromUserEntity(userMap.get(bookBuddy.getUserId()))).timestamp(bookBuddy.getTimestamp()).build()).toList())
                .requests(userEntity.getRequestWrapper().requests().stream()
                    .map(request -> RequestResponse.builder()
                        .userResponse(UserResponse.fromUserEntity(userMap.get(request.getUserId())))
                        .timestamp(request.getTimestamp())
                        .build())
                    .toList())
                .build();
    }
}
