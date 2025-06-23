package com.backend.communityservice.domain.dto.response;

import com.backend.communityservice.common.domain.Category;
import com.backend.communityservice.domain.entity.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostDetailResponse {

    @JsonProperty("username")
    private String username;

    @JsonProperty("question_id")
    private String questionId;

    private String content;

    private String title;

    private Category category;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public static PostDetailResponse fromEntity(Post post, String username) {
        return PostDetailResponse.builder()
                .username(username)
                .questionId(post.getCsQuestionId())
                .content(post.getContent())
                .title(post.getTitle())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
