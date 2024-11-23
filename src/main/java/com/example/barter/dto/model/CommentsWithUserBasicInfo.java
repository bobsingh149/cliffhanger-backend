package com.example.barter.dto.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CommentsWithUserBasicInfo {
    private final List<CommentModel> comments;
    private final List<UserBasicInfo> userBasicInfos;

    @Data
    @Builder
    public static class UserBasicInfo {
        private final String id;
        private final String name;
        private final String profileImage;
    }
} 