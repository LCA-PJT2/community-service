package com.backend.communityservice.domain.repository;

import com.backend.communityservice.domain.entity.Comment;
import com.backend.communityservice.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    void deleteByUserId(String userId);
}
