package com.example.barter.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {

  private final  UserResponse userResponse;
  private final PostResponse postResponse;

  public static UserProfileResponse getInstance(UserResponse userResponse, PostResponse postResponse)
  {
      return UserProfileResponse.builder()
              .postResponse(postResponse)
              .userResponse(userResponse)
              .build();
  }
}
