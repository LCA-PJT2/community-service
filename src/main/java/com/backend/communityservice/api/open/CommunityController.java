package com.backend.communityservice.api.open;

import com.backend.communityservice.common.domain.dto.ApiResponseDto;
import com.backend.communityservice.common.web.context.GatewayRequestHeaderUtils;
import com.backend.communityservice.domain.dto.request.PostRequest;
import com.backend.communityservice.domain.dto.response.PostCreateResponse;
import com.backend.communityservice.domain.entity.Post;
import com.backend.communityservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/community/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommunityController {

    private final PostService postService;

    @GetMapping("/test")
    public ApiResponseDto<String> test() {
        return ApiResponseDto.createOk("community service online");
    }

}
