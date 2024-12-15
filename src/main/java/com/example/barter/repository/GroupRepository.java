package com.example.barter.repository;

import com.example.barter.dto.entity.GroupEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface GroupRepository extends R2dbcRepository<GroupEntity,UUID> {

    @Query("""
            insert into groups
            (name,description,icon) values
            (:name,:description,:icon)
            """)
    Mono<Void> saveGroup(String name, String description, String icon);
}
