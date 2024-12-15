package com.example.barter.dto.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("book_buddy")
public class BookBuddyEntity {
    private final String id;
    private final String name;
    private final int age;
    private final String bio;
    private final String city;
    private final String profileImage;
    private final int commonSubjectCount;
} 