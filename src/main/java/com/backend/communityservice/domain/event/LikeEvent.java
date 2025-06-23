package com.backend.communityservice.domain.event;

import com.backend.communityservice.common.domain.Topics;
import com.backend.communityservice.domain.entity.Comment;
import com.backend.communityservice.domain.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeEvent {

    public static final String Topic = Topics.Like.toString();

    private String action;
    private String userId;
//    private Long postId;
    private Long commentId;
    private Long likeId;
    private LocalDateTime eventTime;

    public static LikeEvent fromEntity(String action, Like like) {
        LikeEvent likeEvent = new LikeEvent();

        likeEvent.action = action;
        likeEvent.userId = like.getUserId();
//        likeEvent.postId = like.getPost().getId();
        likeEvent.commentId = like.getComment().getId();
        likeEvent.likeId = like.getId();
        likeEvent.eventTime = LocalDateTime.now();

        return likeEvent;
    }

}
