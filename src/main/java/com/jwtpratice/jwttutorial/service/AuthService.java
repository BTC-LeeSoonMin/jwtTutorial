package com.jwtpratice.jwttutorial.service;

import com.jwtpratice.jwttutorial.dto.request.SignUpRequestDto;
import com.jwtpratice.jwttutorial.dto.response.auth.SignUpResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    // super는 SignUpResponseDto에 대한 부모 타입도 같이 반환
    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);


}
