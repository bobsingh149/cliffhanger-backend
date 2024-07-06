package com.example.barter.dto.input;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class SaveUserInput {
    private UUID id;
   private String name;
   private String profileImage;
   private int age;


}
