package com.example.barter.dto.entity;

import com.example.barter.dto.input.SaveUserInput;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Data
@Table("users")
@Builder
public class UserEntity {

    @Id
  private final UUID id;

    @NonNull
  private final String name;

   private final int age;

  private final   String[] products;

    @Column("profileimage")
private final String profileImage;

  private final UUID[] connections;


    public static UserEntity fromSaveUserInput(final SaveUserInput saveUserInput)
    {
        final String[] products = {};
        final UUID[] connections = {};

        return UserEntity.builder()
                .id(saveUserInput.id())
                .name(saveUserInput.name())
                .age(saveUserInput.age())
                .profileImage(saveUserInput.profileImage())
                .products(products)
                .connections(connections)
                .build();

    }

}
