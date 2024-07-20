package com.example.barter.dto.response;

import com.example.barter.dto.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserResponse {

    UUID id;
    String name;
    int age;
    String[] products;
    String profileImage;
    UUID[] connections;


    public static UserResponse fromUserEntity(UserEntity userEntity)
    {

        return  UserResponse.builder().id(userEntity.getId())
                .name(userEntity.getName())
                .age(userEntity.getAge())
                .products(userEntity.getProducts())
                .profileImage(userEntity.getProfileImage())
                .connections(userEntity.getConnections())
                .build();

    }

}
