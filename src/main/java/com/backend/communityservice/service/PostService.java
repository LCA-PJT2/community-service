package com.backend.communityservice.service;

import com.backend.communityservice.common.domain.Category;
import com.backend.communityservice.common.exception.BadParameter;
import com.backend.communityservice.common.exception.NotFound;
import com.backend.communityservice.common.exception.Unauthorized;
import com.backend.communityservice.common.web.context.GatewayRequestHeaderUtils;
import com.backend.communityservice.domain.dto.request.PostRequest;
import com.backend.communityservice.domain.dto.response.PostCreateResponse;
import com.backend.communityservice.domain.dto.response.PostDetailResponse;
import com.backend.communityservice.domain.dto.response.PostListResponse;
import com.backend.communityservice.domain.dto.response.PostUpdateResponse;
import com.backend.communityservice.domain.entity.Post;
import com.backend.communityservice.domain.event.PostEvent;
import com.backend.communityservice.domain.repository.PostRepository;
import com.backend.communityservice.event.KafkaMessageProducer;
//import com.backend.communityservice.feign.csquestion.CSQuestionClientService;
//import com.backend.communityservice.feign.user.UserClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
//    private final CSQuestionClientService csQuestionClientService;
    private final KafkaMessageProducer kafkaMessageProducer;
//    private final UserClientService userClientService;

    public PostCreateResponse createPost(PostRequest.PostCreateRequest requestDto,
                                         String userId) {

//        String csQuestionId = "";
        if (requestDto.getQuestionId() != null) {
//            boolean questionExists = csQuestionClientService.existsById(requestDto.getQuestionId());

            boolean questionExists = true; // 👉 항상 존재한다고 가정

            if (!questionExists) {
                throw new NotFound("질문이 존재하지 않습니다.");
            }
        }

        Category category;
        try {
            category = Arrays.stream(Category.values())
                    .filter(c -> c.name().equals(requestDto.getCategory()))
                    .findFirst()
                    .orElseThrow(() -> new BadParameter("유효하지 않은 카테고리입니다."));
        } catch (Exception e) {
            throw new BadParameter("유효하지 않은 카테고리입니다.");
        }


        Post post = requestDto.toEntity(userId, category);
        postRepository.save(post);

        PostEvent postEvent = PostEvent.fromEntity("질문 게시글 생성", post);
        kafkaMessageProducer.send(PostEvent.Topic, postEvent);

        return PostCreateResponse.fromEntity(post);
    }


    public PostUpdateResponse updatePost(PostRequest.PostUpdateRequest requestDto,
                                         Long postId, String userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFound("질문 게시글이 존재하지 않습니다."));

        // 게시글 작성자인지 확인하는 로직
        if (!post.getUserId().equals(userId)) {
            throw new Unauthorized("작성자만 수정할 수 있습니다.");
        }

        if (requestDto.getQuestionId() != null) {
//            boolean questionExists = csQuestionClientService.existsById(requestDto.getQuestionId());
            boolean questionExists = true; // 👉 항상 존재한다고 가정

            if (!questionExists) {
                throw new NotFound("질문이 존재하지 않아요.");
            }
        }

        Category category;
        try {
            category = Arrays.stream(Category.values())
                    .filter(c -> c.name().equals(requestDto.getCategory()))
                    .findFirst()
                    .orElseThrow(() -> new BadParameter("유효하지 않은 카테고리입니다."));
        } catch (Exception e) {
            throw new BadParameter("유효하지 않은 카테고리입니다.");
        }


        post.updatePost(
                category,
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getQuestionId());

        return PostUpdateResponse.fromEntity(post);
    }

    public void deletePost(Long postId, String userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFound("삭제할 게시글이 존재하지 않습니다."));

        // 게시글 작성자인지 확인하는 로직
        if (!post.getUserId().equals(userId)) {
            throw new Unauthorized("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    // 게시글 목록 조회
    public PostListResponse getPostList(Pageable pageable) {

        Page<Post> posts = postRepository.findAll(pageable);

        List<String> userIds = posts.getContent().stream()
                .map(Post::getUserId)
                .distinct()
                .toList();

//        Map<String, String> userMap = userClientService.getUsernames(userIds);

        // 하드코딩된 userMap 생성 (userId → username)
        Map<String, String> userMap = new HashMap<>();
        userMap.put("1", "테스트유저"); // userId "1" → 닉네임 "테스트유저"

        return PostListResponse.fromPosts(posts, userMap);
    }

    // 카테고리별 목록 조회
    public PostListResponse getPostListByCategory(String category, Pageable pageable) {

        Category enumCategory = Category.valueOf(category.toUpperCase());

        Page<Post> posts = postRepository.findByCategory(enumCategory, pageable);

        List<String> userIds = posts.getContent().stream()
                .map(Post::getUserId)
                .distinct()
                .toList();

//        Map<String, String> userMap = userClientService.getUsernames(userIds);

        // 하드코딩된 userMap 생성 (userId → username)
        Map<String, String> userMap = new HashMap<>();
        userMap.put("1", "테스트유저"); // userId "1" → 닉네임 "테스트유저"

        return PostListResponse.fromPosts(posts, userMap);
    }

    // 게시글 상세 조회
    public PostDetailResponse getPostDetail(Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFound("게시글을 찾을수 없습니다."));

//        String username = userClientService.getUserInfo(post.getUserId()).getUserName();

        String username = "김수현";

        return PostDetailResponse.fromEntity(post, username);
    }
}













