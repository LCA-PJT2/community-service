//package com.backend.communityservice.feign.user;
//
//import com.backend.communityservice.feign.user.dto.UserInfoDto;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//import java.util.Map;
//
//@FeignClient(name = "user-service",
////            url = "http://user-service:8080",
//            // TODO: user service api 넣기
//            path = "user service api")
//public interface UserClientService {
//
//    // TODO: 사용자 정보 요청 api 넣기
//    @PostMapping("/user/usernames")
//    Map<String, String> getUsernames(@RequestBody List<String> userIds);
//
//    @GetMapping("/user/username")
//    UserInfoDto.UserResponse getUserInfo(@RequestParam("userId") String userId);
//
//}