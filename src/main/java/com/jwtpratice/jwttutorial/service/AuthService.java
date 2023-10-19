package com.jwtpratice.jwttutorial.service;

import com.jwtpratice.jwttutorial.entity.UserEntity;

import java.util.Map;

public interface AuthService {

    // super는 SignUpResponseDto에 대한 부모 타입도 같이 반환
    String signUp(Map<String, Object> msgMap, UserEntity userEntity);


    Map<String,Object> signIn(Map<String, Object> msgMap, UserEntity userEntity);
}
