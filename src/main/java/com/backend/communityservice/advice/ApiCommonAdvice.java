package com.backend.communityservice.advice;

import com.backend.communityservice.common.domain.dto.ApiResponseDto;
import com.backend.communityservice.common.exception.BadParameter;
import com.backend.communityservice.common.exception.ClientError;
import com.backend.communityservice.common.exception.NotFound;
import com.backend.communityservice.common.exception.Unauthorized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@Order(value = 1)
@RestControllerAdvice
public class ApiCommonAdvice {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({Unauthorized.class})
    public ApiResponseDto<String> handleBadParameter(Unauthorized e) {
        e.printStackTrace();
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadParameter.class})
    public ApiResponseDto<String> handleBadParameter(BadParameter e) {
        e.printStackTrace();
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFound.class})
    public ApiResponseDto<String> handleNotFound(NotFound e) {
        e.printStackTrace();
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ClientError.class})
    public ApiResponseDto<String> handleClientError(ClientError e) {
        e.printStackTrace();
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoResourceFoundException.class})
    public ApiResponseDto<String> handleNoResourceFoundException(NoResourceFoundException e) {
        e.printStackTrace();
        return ApiResponseDto.createError(
                "NoResource",
                "리소스를 찾을 수 없습니다."
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // 모든 Exception을 받고 있는 상태임
    @ExceptionHandler({Exception.class})
    public ApiResponseDto<String> handleException(Exception e) {
        e.printStackTrace();
        return ApiResponseDto.createError(
                "ServerError",
                "서버 에러입니다."
        );
    }
}