package com.example.barter.dto.input;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SaveUserInput {


    private String id;
   private String name;
   private String email;
   private String profileImage;
   private int age;


}
