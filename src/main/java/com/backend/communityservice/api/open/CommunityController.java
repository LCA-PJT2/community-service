package com.backend.communityservice.api.open;

import com.backend.communityservice.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/community/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommunityController {

    @GetMapping("/test")
    public ApiResponseDto<String> test() {
        return ApiResponseDto.createOk("community service online");
    }

}
