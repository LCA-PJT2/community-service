package com.backend.communityservice.domain.repository;

import com.backend.communityservice.domain.entity.Comment;
import com.backend.communityservice.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByCommentAndUserId(Comment comment, String userId);

    void deleteByComment(Comment comment);
}
