package com.backend.communityservice.service;

import com.backend.communityservice.common.exception.NotFound;
import com.backend.communityservice.domain.dto.response.LikeResponse;
import com.backend.communityservice.domain.entity.Comment;
import com.backend.communityservice.domain.entity.Like;
import com.backend.communityservice.domain.event.LikeEvent;
import com.backend.communityservice.domain.repository.CommentRepository;
import com.backend.communityservice.domain.repository.LikeRepository;
import com.backend.communityservice.event.KafkaMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final KafkaMessageProducer kafkaMessageProducer;

    public LikeResponse toggleLike(Long commentId, String userId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFound("댓글이 존재하지 않습니다."));

        Optional<Like> existingLike = likeRepository.findByCommentAndUserId(comment, userId);

        if (existingLike.isPresent()) {

            likeRepository.delete(existingLike.get());

            return LikeResponse.fromEntity(comment.getId(), false);

        } else {

            Like like = Like.builder()
                    .userId(userId)
                    .comment(comment)
                    .createdAt(LocalDateTime.now())
                    .build();

            likeRepository.save(like);

            LikeEvent likeEvent = LikeEvent.fromEntity("좋아요 등록", like);
            kafkaMessageProducer.send(LikeEvent.Topic,  likeEvent);

            return LikeResponse.fromEntity(comment.getId(), true);
        }
    }
}
