package com.jwtpratice.jwttutorial.service;

import com.jwtpratice.jwttutorial.dao.IMemberDaoMapper;
import com.jwtpratice.jwttutorial.entity.RefTokenEntity;
import com.jwtpratice.jwttutorial.entity.UserEntity;
import com.jwtpratice.jwttutorial.filter.JwtAuthenticationFilter;
import com.jwtpratice.jwttutorial.provider.JwtProvider;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    @Value("${secret-key}")
    private String secretKey;

    @Autowired
    IMemberDaoMapper iMemberDaoMapper;

    // @RequiredArgsConstructor을 이용하면 final로 지정된 것은 필수 생성자로 여긴다
    private final JwtProvider jwtProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //
    @Override
    public String signUp(Map<String, Object> msgMap, UserEntity userEntity) {
        System.out.println("[AuthServiceImplement] signUp");

        userEntity.setEmail(msgMap.get("email").toString());
        userEntity.setM_password(passwordEncoder.encode(msgMap.get("m_password").toString()));
        userEntity.setNickname(msgMap.get("nickname").toString());


        int result = -1;
        result = iMemberDaoMapper.insertMember(userEntity);
        if (result > 0) {
            System.out.println("SIGN IN SUCCESS");
            return "SIGN IN SUCCESS";
        } else {
            System.out.println("SIGN IN FAIL");
            return "SIGN IN FAIL";
        }

    }

    @Override
    public Map<String, Object> signIn(Map<String, Object> msgMap, UserEntity userEntity, RefTokenEntity refTokenEntity) {
        System.out.println("[AuthServiceImplement] signIn");

        Map<String, Object> map = new HashMap<>();

        // 동일한 username 없음
        UserEntity idVerifiedUserEntity = iMemberDaoMapper.isMember(userEntity);

        if (idVerifiedUserEntity != null && passwordEncoder.matches(userEntity.getM_password(), idVerifiedUserEntity.getM_password())) {
            String accessToken = jwtProvider.createAccessToken(userEntity.getEmail(), secretKey);
            String refreshToken = jwtProvider.createRefreshToken(userEntity.getEmail(), secretKey);

            refTokenEntity.setRef_token(refreshToken);
            log.info("tp : "+ refTokenEntity.getRef_token());
            // refresh token -> tbl_tokens에 저장
            int result = iMemberDaoMapper.insertRefToken(refTokenEntity);
            if(result <= 0){
                 log.info("Ref Token 등록 실패");
            } else {
                 log.info("Ref Token 등록 성공");

            }

            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            return map;

        } else {
            map.put("result", HttpStatus.NOT_FOUND);
            return map;

        }

    }

    @Override
    public Map<String, Object> refreshToken(HttpServletRequest request, HttpServletResponse response, RefTokenEntity refTokenEntity) {
        System.out.println("[AuthServiceImplement] refreshToken");

        Map<String, Object> map = new HashMap<>();
        final String authHeader = request.getHeader(HttpHeaders.COOKIE);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null) {
            map.put("result", HttpStatus.NOT_FOUND);
            return map;
        }
        String cookieToken = authHeader.substring(7);
        refreshToken = cookieToken.split("=")[1];

        // token에 동일한 refresh token 명이 있는지 check
        // 있으면 이후 작업 진행,
        // 없으면 이미 로그아웃 또는 회원 탈퇴를 진행한 회원이라고 판단했기 때문에 오류 코드 발생.
        refTokenEntity.setRef_token(refreshToken);
        RefTokenEntity checkRefToken = iMemberDaoMapper.selectRefToken(refTokenEntity);
        log.info("tp : {}", checkRefToken);
        if(checkRefToken == null) {
            map.put("result", "nullCheckRefToken");
            return map;
        }

        userEmail = jwtAuthenticationFilter.getUserEmail(secretKey, refreshToken);
        if (userEmail != null) {
            if (jwtAuthenticationFilter.validate(secretKey, refreshToken)) {
                log.error("refreshToken이 만료되었습니다.");
//                jwtExceptionHandler(response, ErrorType.NOT_VALID_TOKEN);
            } else {
                String ReAccessToken = jwtProvider.createAccessToken(userEmail, secretKey);
                String ReRefreshToken = jwtProvider.createRefreshToken(userEmail, secretKey);
                map.put("ReAccessToken", ReAccessToken);
                map.put("ReRefreshToken", ReRefreshToken);
                return map;
            }
        }
        return map;
    }

}
