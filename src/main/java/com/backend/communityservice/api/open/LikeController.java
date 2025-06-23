package com.backend.communityservice.api.open;

import com.backend.communityservice.common.domain.dto.ApiResponseDto;
import com.backend.communityservice.common.web.context.GatewayRequestHeaderUtils;
import com.backend.communityservice.domain.dto.response.LikeResponse;
import com.backend.communityservice.domain.repository.LikeRepository;
import com.backend.communityservice.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/like/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{commentId}/like")
    public ApiResponseDto<LikeResponse> toggleLike(@PathVariable("commentId") Long commentId) {

//        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        String userId = "1";

        LikeResponse response = likeService.toggleLike(commentId, userId);

        return ApiResponseDto.createOk(response);
    }
}
