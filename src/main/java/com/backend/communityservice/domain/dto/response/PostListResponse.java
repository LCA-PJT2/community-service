package com.backend.communityservice.domain.dto.response;

import com.backend.communityservice.common.domain.Category;
import com.backend.communityservice.domain.entity.Post;
//import com.backend.communityservice.feign.user.UserClientService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class PostListResponse {

    private List<PostSummary> posts;

    @Getter
    @Builder
    public static class PostSummary {

        private Long id;

        private String title;

        private Category category;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("username")
        private String username;

        @JsonProperty("comment_count")
        private int commentCount;

        public static PostSummary fromEntity(Post post, String username) {
            return PostSummary.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .category(post.getCategory())
                    .createdAt(post.getCreatedAt())
                    .username(username)
                    .commentCount(post.getComments().size())
                    .build();
        }
    }

    public static PostListResponse fromPosts(Page<Post> postPage, Map<String, String> userMap) {
        List<PostSummary> postSummaries = postPage.getContent().stream()
                .map(post -> {
                    String username = userMap.getOrDefault(post.getUserId(), "알수없음");
                    return PostListResponse.PostSummary.fromEntity(post, username);
                })
                .toList();

        return PostListResponse.builder()
                .posts(postSummaries)
                .build();
    }
}
