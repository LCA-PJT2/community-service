package com.backend.communityservice.feign.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoDto {

    @Data
    public static class UserRequest {
        private String userId;
    }

    @Data
    public static class UserResponse {
        private String userId;
        private String userName;
    }

}
