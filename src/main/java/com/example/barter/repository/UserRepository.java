package com.example.barter.repository;

import com.example.barter.dto.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

        @Query("update users set name = :name, age = :age, profileimage = :profileImage where id = :id returning *")
        Flux<UserEntity> update(String id, String name, int age, String profileImage);

        @Query("insert into users(id,name,age,products,profileimage,connections) values (:id,:name,:age,:products,:profileImage,:connections)")
        Mono<Void> save(String id, String name, int age, List<String> products,String profileImage,List<String> connections);

}
