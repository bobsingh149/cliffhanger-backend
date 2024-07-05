//package com.example.barter.dto.entity;
//
//import lombok.Builder;
//import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.relational.core.mapping.Table;
//import org.springframework.lang.NonNull;
//
//import java.util.List;
//import java.util.UUID;
//
//@Data
//@Builder
//@Table("user")
//public class UserEntity {
//
//    @Id
//    String id;
//
//    @NonNull
//    String name;
//
//    int age;
//
//    List<String> products;
//
//    String profile;
//
//    List<UUID> connections';'
//
//}
//
//
////CREATE TABLE user
////        (
////                id UUID primary key,
////                name varchar not null,
////                age int,
////                products jsonb,
////                profile varchar,
////                connections jsonb
////        );