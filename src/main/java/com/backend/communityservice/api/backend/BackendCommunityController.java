package com.backend.communityservice.api.backend;

import com.backend.communityservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/backend/community/v1")
public class BackendCommunityController {

    private final PostService postService;

    @PostMapping("/user/clear/{userId}")
    public void deleteEverything(@PathVariable String userId) {
        postService.deleteEverythingByUserId(userId);
    }
}
