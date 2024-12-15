package com.example.barter.dto.response;

import com.example.barter.dto.model.PostCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserProfileResponse {

  private final  UserResponse userInfo;
  private final List<PostResponse> posts;
  private final UserStats userStats;

  @Data
  @Builder
  public static  class UserStats
  {
    private final long postCount;
    private final long bookBuddyCount;
    private final long barterCount;
  }

  public static UserProfileResponse getInstance(UserResponse userResponse, List<PostResponse> postResponse)
  {
      return UserProfileResponse.builder()
              .posts(postResponse)
              .userInfo(userResponse)
              .userStats(UserStats.builder().postCount(postResponse.size())
                      .barterCount(postResponse.stream().filter(post->post.getCategory() == PostCategory.barter).count())
                      .bookBuddyCount(userResponse.getBookBuddyCount())
                      .build())
              .build();
  }


}
