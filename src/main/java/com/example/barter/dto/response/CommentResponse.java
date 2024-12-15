package com.example.barter.dto.response;

import com.example.barter.dto.model.CommentModel;
import com.example.barter.dto.model.CommentsWithUserBasicInfo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class CommentResponse {
    private final String id;
    private final String text;
    private final CommentsWithUserBasicInfo.UserBasicInfo userBasicInfo;
    private final LocalDateTime timestamp;
    private final int likeCount;

    public  static  CommentResponse fromCommentModel(CommentModel commentModel, Map<String, CommentsWithUserBasicInfo.UserBasicInfo> basicInfoMap)
    {
      return CommentResponse.builder()
              .id(commentModel.getId())
              .text(commentModel.getText())
              .userBasicInfo(basicInfoMap.get(commentModel.getUserId()))
              .timestamp(commentModel.getTimestamp())
              .likeCount(commentModel.getLikeCount())
              .build();
    }
}
