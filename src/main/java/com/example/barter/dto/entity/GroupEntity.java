package com.example.barter.dto.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("groups")
@Data
@Builder
public class GroupEntity {

    @Id
    private final UUID id;
    private final String name;
    private final String description;
    private final String icon;
}
