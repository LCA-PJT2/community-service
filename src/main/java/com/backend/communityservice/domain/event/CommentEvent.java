package com.backend.communityservice.domain.event;

import com.backend.communityservice.common.domain.Topics;
import com.backend.communityservice.domain.entity.Comment;
import com.backend.communityservice.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {

    public static final String Topic = Topics.Comment.toString();

    private String action;
    private String userId;
    private Long postId;
    private Long commentId;
    private LocalDateTime eventTime;

    public static CommentEvent fromEntity(String action, Comment comment) {
        CommentEvent commentEvent = new CommentEvent();

        commentEvent.action = action;
        commentEvent.userId = comment.getUserId();
        commentEvent.postId = comment.getPost().getId();
        commentEvent.commentId = comment.getId();
        commentEvent.eventTime = LocalDateTime.now();

        return commentEvent;
    }
}
