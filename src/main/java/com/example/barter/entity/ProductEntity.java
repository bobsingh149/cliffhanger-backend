package com.example.barter.entity;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Data
@Table(name = "product")
@Builder
public class ProductEntity {

    @Id
    private UUID id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    private String category;
    private String image;
    @NonNull
    private int ranking;
}
