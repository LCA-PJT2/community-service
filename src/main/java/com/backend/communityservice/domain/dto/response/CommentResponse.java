package com.backend.communityservice.domain.dto.response;

import com.backend.communityservice.domain.entity.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentResponse {

    @Data
    @Builder
    public static class CommentCreateResponse {

        private Long id;

        @JsonProperty("user_id")
        private String userId;

        @JsonProperty("post_id")
        private Long postId;

        private String content;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        public static CommentCreateResponse fromEntity(Comment comment) {
            return CommentCreateResponse.builder()
                    .id(comment.getId())
                    .userId(comment.getUserId())
                    .postId(comment.getPost().getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .build();
        }
    }

    @Data
    @Builder
    public static class CommentUpdateResponse {
        private Long id;

        @JsonProperty("user_id")
        private String userId;

        @JsonProperty("post_id")
        private Long postId;

        private String content;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        public static CommentUpdateResponse fromEntity(Comment comment) {
            return CommentUpdateResponse.builder()
                    .id(comment.getId())
                    .userId(comment.getUserId())
                    .postId(comment.getPost().getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .build();
        }
    }

    @Data
    @Builder
    public static class CommentListResponse {

        private List<CommentSummary> comments;

        @Data
        @Builder
        public static class CommentSummary {

            @JsonProperty("comment_id")
            private Long commentId;

            private String username;

            private String content;

            @JsonProperty("created_at")
            private LocalDateTime createdAt;

            public static CommentSummary fromEntity(Comment comment, String username) {
                return CommentSummary.builder()
                        .commentId(comment.getId())
                        .username(username)
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build();
            }
        }

        public static CommentListResponse fromComments(List<Comment> comments, Map<String, String> userMap) {
            List<CommentSummary> commentSummaries = comments.stream()
                    .map(comment -> {
                        String username = userMap.getOrDefault(comment.getUserId(), "알수없음");
                        return CommentListResponse.CommentSummary.fromEntity(comment, username);
                    })
                    .toList();

            return CommentListResponse.builder()
                    .comments(commentSummaries)
                    .build();

        }
    }
}
