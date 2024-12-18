package com.example.barter.dto.response;

import com.example.barter.dto.entity.UserEntity;
import com.example.barter.dto.model.RequestModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Collections;

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

    public static List<RequestResponse> sortRequests(List<RequestModel> requests, Map<String, UserEntity> userMap)
    {
        List<RequestResponse> requestResponses = new java.util.ArrayList<>(requests.stream()
                .map(request -> RequestResponse.builder()
                        .userResponse(UserResponse.fromUserEntity(userMap.get(request.getUserId())))
                        .timestamp(request.getTimestamp())
                        .build())
                .toList());
        
        Collections.sort(requestResponses);
        return requestResponses;
    }

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
                        .userResponse(userMap.get(conversation.getUserId())!= null ?UserResponse.fromUserEntity(userMap.get(conversation.getUserId())):null)
                        .members(conversation.getMembers().stream().map(id->UserResponse.fromUserEntity(userMap.get(id))).toList()).build()
                ).toList())
                .bookBuddies(userEntity.getBookBuddyWrapper().bookBuddies().stream()
                        .map(bookBuddy->BookBuddyResponse.builder().
                        userResponse(UserResponse.fromUserEntity(userMap.get(bookBuddy.getUserId())))
                        .timestamp(bookBuddy.getTimestamp())
                        .commonSubjectCount( bookBuddy.getCommonSubjectCount()*3 + 10)
                        .build()).toList())
                .requests(sortRequests(userEntity.getRequestWrapper().requests(),userMap))
                .build();
    }
}
