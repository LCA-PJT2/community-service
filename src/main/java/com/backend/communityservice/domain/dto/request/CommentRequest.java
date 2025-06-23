package com.backend.communityservice.domain.dto.request;

import com.backend.communityservice.common.domain.Category;
import com.backend.communityservice.domain.entity.Comment;
import com.backend.communityservice.domain.entity.Post;
import lombok.Builder;
import lombok.Data;

public class CommentRequest {

    @Data
    @Builder
    public static class CommentCreateRequest{
        private String content;

        public Comment toEntity(Post post, String userId) {
            return Comment.builder()
                    .userId(userId)
                    .post(post)
                    .content(this.content)
                    .build();
        }
    }

    @Data
    @Builder
    public static class CommentUpdateRequest{
        private String content;
    }
}
