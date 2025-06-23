package com.backend.communityservice.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeResponse {
    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("is_liked")
    private Boolean isLiked;

    public static LikeResponse fromEntity(Long commentId, boolean isLiked) {
        return LikeResponse.builder()
                .commentId(commentId)
                .isLiked(isLiked)
                .build();
    }
}
