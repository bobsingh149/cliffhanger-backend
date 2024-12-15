package com.example.barter.dto.response;

import com.example.barter.dto.entity.UserEntity;
import com.example.barter.dto.model.ConversationModel;
import com.example.barter.dto.model.BookBuddyModel;
import com.example.barter.dto.model.RequestModel;

import java.util.List;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

import com.example.barter.exception.customexception.UserNotFoundException;

@Data
@Builder
public class UserResponse {

    private final String id;
    private final String name;
    private final int age;
    private final String bio;
    private final String city;
    private final String profileImage;
    private final int bookBuddyCount;

    public static UserResponse fromUserEntity(UserEntity userEntity) {
        if (userEntity == null) {
            throw new UserNotFoundException("User not found");
        }
        return UserResponse.builder().id(userEntity.getId())
                .name(userEntity.getName())
                .age(userEntity.getAge())
                .profileImage(userEntity.getProfileImage())
                .bio(userEntity.getBio())
                .city(userEntity.getCity())
                .bookBuddyCount(userEntity.getBookBuddyWrapper().bookBuddies().size())
                .build();

    }



}
