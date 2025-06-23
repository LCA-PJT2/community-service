package com.backend.communityservice.domain.dto.response;

import com.backend.communityservice.common.domain.Category;
import com.backend.communityservice.domain.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostUpdateResponse {

    private Long id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("question_id")
    private String questionId;

    private String title;

    private String content;

    private Category category;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public static PostUpdateResponse fromEntity(Post post) {
        return PostUpdateResponse.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .questionId(post.getCsQuestionId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
