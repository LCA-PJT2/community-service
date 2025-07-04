package com.backend.communityservice.feign.csquestion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "qna-service",
            url = "http://qna-service-service:8080",
            path = "/backend/qna/v1")
public interface CSQuestionClientService {

    @GetMapping("/{id}/exists")
    boolean existsById(@PathVariable("id") Long questionId);
}
