package com.example.barter.repository;

import com.example.barter.dto.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

    @Query("update users set name = :name, age = :age, profileimage = :profileImage where id = :id returning *")
    Mono<UserEntity> updateUser(String id, String name, int age, String profileImage);

    @Query("insert into users(id,name,age,products,profileimage,connections) values (:id,:name,:age,:products,:profileImage,:connections)")
    Mono<Void> saveUser(String id, String name, int age, String[] products, String profileImage, String[] connections);

    @Query("""
            update users
            set connections =
            case
            when id = :id
            then array_append(connections,:connectionId)
            when id = :connectionId
            then array_append(connections, :id)
            else
            connections
            end;
            """)
    Mono<Void> saveConnection(String id, String connectionId);

    @Query("update users set requests = array_append(requests,:requestId) where id = :id")
    Mono<Void> saveRequest(String id, String requestId);

    @Query("""
            with user_connections as
            (select connections from users where id = :id)
            select u.*
            from users as u, user_connections as uc
            where u.id = any(uc.connections);
            """)
    Flux<UserEntity> getConnections(String id);


    @Query("""
            with user_requests as
            (select requests from users where id = :id)
            select u.*
            from users as u, user_requests as ur
            where u.id = any(ur.requests);
            """)
    Flux<UserEntity> getRequests(String id);


    @Query("""
            with user_products as
            (select products from users where id = :id)
            ,
            user_connections as
            (select connections from users where id = :id)
            ,
            common_products as(
            select u.*,
            (select count(*) from unnest(u.products) as product where product = any(up.products)) as common_count,
            (select array_agg(product) from unnest(u.products) as product where product = any(up.products)) as common_books
            from users as u, user_products as up, user_connections as uc
            where not u.id = :id
            and not u.id = any(uc.connections)
            )
            select cp.*
            from common_products as cp, users as u
            order by cp.common_count desc,u.score desc offset :offset limit :size;
            """)
    Flux<UserEntity> getCommonUsers(String id, long offset, int size);
}


