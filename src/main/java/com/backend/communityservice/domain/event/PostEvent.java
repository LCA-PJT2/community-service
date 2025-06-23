package com.backend.communityservice.domain.event;

import com.backend.communityservice.common.domain.Topics;
import com.backend.communityservice.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostEvent {

    public static final String Topic = Topics.Post.toString();

    private String action;
    private String userId;
    private String title;
    private LocalDateTime eventTime;

    public static PostEvent fromEntity(String action, Post post) {
        PostEvent postEvent = new PostEvent();

        postEvent.action = action;
        postEvent.userId = post.getUserId();
        postEvent.title = post.getTitle();
        postEvent.eventTime = LocalDateTime.now();

        return postEvent;
    }
}
