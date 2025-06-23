package com.backend.communityservice.domain.dto.request;

import com.backend.communityservice.common.domain.Category;
import com.backend.communityservice.domain.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

public class PostRequest {

    @Data
    @Builder
    public static class PostCreateRequest {

        private String title;

        @JsonProperty("question_id")
        private String questionId;
        private String content;
        private String category;

        public Post toEntity(String userId, Category category) {
            return Post.builder()
                    .userId(userId)
                    .csQuestionId(this.questionId)
                    .category(category)
                    .title(this.title)
                    .content(this.content)
                    .build();
        }
    }

    @Data
    @Builder
    public static class PostUpdateRequest {

        private String title;

        @JsonProperty("question_id")
        private String questionId;
        private String content;
        private String category;
    }
}
