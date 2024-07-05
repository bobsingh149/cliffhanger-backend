package com.example.barter.dto.response;

import com.example.barter.dto.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponse {

    String id;
    String name;
    int age;
    List<String> products;
    String profileImage;
    List<String> connections;


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

/**
 *
 @Id
 String id;

 @NonNull
 String name;

 int age;

 List<String> products;

 String profile;

 List<String> connections;

 */