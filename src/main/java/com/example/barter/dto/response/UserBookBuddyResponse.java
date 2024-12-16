package com.example.barter.dto.response;

import com.example.barter.dto.entity.BookBuddyEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserBookBuddyResponse {
    private final String id;
    private final String name;
    private final int age;
    private final String bio;
    private final String city;
    private final String profileImage;
    private final int commonSubjectCount;


    public static UserBookBuddyResponse fromBookBuddyEntity(BookBuddyEntity bookBuddyEntity){
        return UserBookBuddyResponse.builder().id(bookBuddyEntity.getId()).name(bookBuddyEntity.getName()).age(bookBuddyEntity.getAge()).bio(bookBuddyEntity.getBio()).city(bookBuddyEntity.getCity()).profileImage(bookBuddyEntity.getProfileImage()).commonSubjectCount(bookBuddyEntity.getCommonSubjectCount()*3 + 10omm).build();
    }
} 