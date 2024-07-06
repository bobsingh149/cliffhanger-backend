package com.example.barter.dto.input;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SaveUserInput {

    private String id;
   private String name;
   private String profileImage;
   private int age;


}
