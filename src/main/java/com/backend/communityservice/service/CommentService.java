package com.backend.communityservice.service;

import com.backend.communityservice.common.exception.NotFound;
import com.backend.communityservice.common.exception.Unauthorized;
import com.backend.communityservice.domain.dto.request.CommentRequest;
import com.backend.communityservice.domain.dto.response.CommentResponse;
import com.backend.communityservice.domain.entity.Comment;
import com.backend.communityservice.domain.entity.Post;
import com.backend.communityservice.domain.event.CommentEvent;
import com.backend.communityservice.domain.repository.CommentRepository;
import com.backend.communityservice.domain.repository.LikeRepository;
import com.backend.communityservice.domain.repository.PostRepository;
import com.backend.communityservice.event.KafkaMessageProducer;
//import com.backend.communityservice.feign.user.UserClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final KafkaMessageProducer kafkaMessageProducer;
//    private final UserClientService userClientService;
    private final LikeRepository likeRepository;

    public CommentResponse.CommentListResponse getCommentList(Long postId) {
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFound("게시글이 존재하지 않습니다."));

        List<Comment> comments = commentRepository.findByPost(post);

        List<String> userIds = comments.stream()
                .map(Comment::getUserId)
                .distinct()
                .toList();

//        Map<String, String> userMap = userClientService.getUsernames(userIds);

        // 하드코딩된 userMap 생성 (userId → username)
        Map<String, String> userMap = new HashMap<>();
        userMap.put("1", "테스트유저"); // userId "1" → 닉네임 "테스트유저"
        
        return CommentResponse.CommentListResponse.fromComments(comments, userMap);
    }

    public CommentResponse.CommentCreateResponse addComment(Long postId, String userId,
                                                            CommentRequest.CommentCreateRequest createDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFound("게시글을 찾을수 없습니다."));

        Comment comment = createDto.toEntity(post, userId);
        commentRepository.save(comment);

        CommentEvent commentEvent = CommentEvent.fromEntity("댓글 생성", comment);
        kafkaMessageProducer.send(CommentEvent.Topic, commentEvent);

        return CommentResponse.CommentCreateResponse.fromEntity(comment);
    }


    @Transactional
    public CommentResponse.CommentUpdateResponse updateComment(Long commentId, String userId,
                                                               CommentRequest.CommentUpdateRequest updateDto) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFound("댓글이 존재하지 않습니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new Unauthorized("작성자만 수정할 수 있습니다.");
        }

        comment.updateComment(updateDto.getContent());

        return CommentResponse.CommentUpdateResponse.fromEntity(comment);
    }

    public void deleteComment(Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFound("댓글이 존재하지 않습니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new Unauthorized("작성자만 수정할 수 있습니다.");
        }

        try {
            likeRepository.deleteByComment(comment);
            log.info("Like 삭제 완료: " + comment.getUserId());

            commentRepository.delete(comment);
            log.info("Comment 삭제 완료: " + comment.getUserId());

        } catch (Exception e) {
            log.error("Comment 삭제 중 오류 발생: " + e);
            throw new RuntimeException("연관된 데이터 삭제 중 오류 발생", e);
        }
    }
}
