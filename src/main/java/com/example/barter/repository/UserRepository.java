package com.example.barter.repository;

import com.example.barter.dto.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

        @Query("update product set name = :name, age = :age, profileimage = :profileImage where id = :id returning *")
        Flux<UserEntity> update(String id, String name, int age, String profileImage);
}

/**
 *    @Id
 *     String id;
 *
 *     @NonNull
 *     String name;
 *
 *     int age;
 *
 *     List<String> products;
 *
 *     @Column("profileimage")
 *     String profileImage;
 *
 *     List<String> connections;
 */
