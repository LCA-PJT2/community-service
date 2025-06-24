package com.backend.communityservice.domain.repository;

import com.backend.communityservice.common.domain.Category;
import com.backend.communityservice.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCategory(Category category, Pageable pageable);

    List<Post> findByUserId(String userId);
}
