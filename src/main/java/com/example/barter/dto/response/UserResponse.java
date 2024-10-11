package com.example.barter.dto.response;

import com.example.barter.dto.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private final  String id;
  private final   String name;
  private final   int age;
  private final   String[] products;
  private final   String profileImage;
  private final   String[] connections;


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
