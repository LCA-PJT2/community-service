package com.backend.communityservice.api.open;

import com.backend.communityservice.common.domain.dto.ApiResponseDto;
import com.backend.communityservice.common.web.context.GatewayRequestHeaderUtils;
import com.backend.communityservice.domain.dto.request.CommentRequest;
import com.backend.communityservice.domain.dto.response.CommentResponse;
import com.backend.communityservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/comment/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ApiResponseDto<CommentResponse.CommentListResponse> getComments(@PathVariable Long postId) {
        CommentResponse.CommentListResponse response = commentService.getCommentList(postId);
        return ApiResponseDto.createOk(response);
    }

    @PostMapping("/create/{postId}")
    public ApiResponseDto<CommentResponse.CommentCreateResponse> addComment(@PathVariable Long postId,
                                                      @RequestBody CommentRequest.CommentCreateRequest commentCreateRequest) {

        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();

//        String userId = "1";

        var response = commentService.addComment(postId, userId, commentCreateRequest);

        return ApiResponseDto.createOk(response);
    }

    @PostMapping("/update/{commentId}")
    public ApiResponseDto<CommentResponse.CommentUpdateResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest.CommentUpdateRequest commentUpdateRequest) {

        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
//        String userId = "1";

        var response = commentService.updateComment(commentId, userId, commentUpdateRequest);

        return ApiResponseDto.createOk(response);
    }

    @PostMapping("/delete/{commentId}")
    public ApiResponseDto<?> deleteComment(@PathVariable Long commentId) {

        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
//        String userId = "1";

        commentService.deleteComment(commentId, userId);

        return ApiResponseDto.defaultOk();
    }
}
