package com.jwtpratice.jwttutorial.entity;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {

    UNKNOWN_ERROR(400,"인증 토큰이 존재하지 않습니다."),
    WRONG_TYPE_TOKEN(400,"잘못된 토큰 정보입니다."),
    EXPIRED_TOKEN(400,"만료된 토큰 정보입니다."),
    UNSUPPORTED_TOKEN(400,"지원하지 않는 토큰 방식입니다."),
    ACCESS_DENIED(400,"알 수 없는 이유로 요청이 거절되었습니다.");

    private int code;
    private String message;

    ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

}
