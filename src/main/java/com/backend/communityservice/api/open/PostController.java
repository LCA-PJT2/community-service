package com.backend.communityservice.api.open;

import com.backend.communityservice.common.domain.dto.ApiResponseDto;
import com.backend.communityservice.common.web.context.GatewayRequestHeaderUtils;
import com.backend.communityservice.domain.dto.request.PostRequest;
import com.backend.communityservice.domain.dto.response.PostCreateResponse;
import com.backend.communityservice.domain.dto.response.PostDetailResponse;
import com.backend.communityservice.domain.dto.response.PostListResponse;
import com.backend.communityservice.domain.dto.response.PostUpdateResponse;
import com.backend.communityservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/post/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ApiResponseDto<PostListResponse> getPostList(
            @RequestParam(required = false) String category,
            Pageable pageable) {

        if (category != null) {
            return ApiResponseDto.createOk(postService.getPostListByCategory(category, pageable));
        } else  {
            return ApiResponseDto.createOk(postService.getPostList(pageable));
        }
    }

    @GetMapping("/{postId}")
    public ApiResponseDto<PostDetailResponse> getPostDetail(@PathVariable Long postId) {

        PostDetailResponse postDetailResponse = postService.getPostDetail(postId);

        return ApiResponseDto.createOk(postDetailResponse);
    }



    @PostMapping("/create")
    public ApiResponseDto<PostCreateResponse> createPost(@RequestBody PostRequest.PostCreateRequest postCreateRequest) {
    
        // gateway header에서 userId 받아옴
//        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        String userId = "1";

        // postcreate 서비스 호출
        PostCreateResponse createResponse = postService.createPost(postCreateRequest, userId);

        return ApiResponseDto.createOk(createResponse);

    }

    @PostMapping("/update/{postId}")
    public ApiResponseDto<PostUpdateResponse> updatePost(@PathVariable Long postId,
                                                         @RequestBody PostRequest.PostUpdateRequest postUpdateRequest) {

        // gateway header에서 userId 받아옴
//        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        String userId = "1";

        PostUpdateResponse updateResponse = postService.updatePost(postUpdateRequest, postId, userId);

        return ApiResponseDto.createOk(updateResponse);
    }

    @PostMapping("/delete/{postId}")
    public ApiResponseDto<?> deletePost(@PathVariable Long postId) {

        // gateway header에서 userId 받아옴
//        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        String userId = "1";

        postService.deletePost(postId, userId);

        return ApiResponseDto.defaultOk();
    }

}
