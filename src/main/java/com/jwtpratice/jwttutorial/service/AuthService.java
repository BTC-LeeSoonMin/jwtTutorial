package com.jwtpratice.jwttutorial.service;

import com.jwtpratice.jwttutorial.entity.RefTokenEntity;
import com.jwtpratice.jwttutorial.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface AuthService {

    // super는 SignUpResponseDto에 대한 부모 타입도 같이 반환
    String signUp(Map<String, Object> msgMap, UserEntity userEntity);

    Map<String,Object> signIn(Map<String, Object> msgMap, UserEntity userEntity, RefTokenEntity refTokenEntity, HttpServletRequest request, HttpServletResponse response);

    Map<String, Object> refreshToken(HttpServletRequest request, HttpServletResponse response, RefTokenEntity refTokenEntity);

    String logout(HttpServletRequest request, HttpServletResponse response, RefTokenEntity refTokenEntity);

    String signOut(HttpServletRequest request, HttpServletResponse response, UserEntity userEntity, RefTokenEntity refTokenEntity);
}
