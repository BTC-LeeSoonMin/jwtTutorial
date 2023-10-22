package com.jwtpratice.jwttutorial.service;

import com.jwtpratice.jwttutorial.dao.IMemberDaoMapper;
import com.jwtpratice.jwttutorial.entity.UserEntity;
import com.jwtpratice.jwttutorial.provider.JwtProvider;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class AuthServiceImplement implements AuthService {

    @Value("${secret-key}")
    private String secretKey;

    @Autowired
    IMemberDaoMapper iMemberDaoMapper;

    @Autowired
    JwtProvider jwtProvider;


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
    public Map<String, Object> signIn(Map<String, Object> msgMap, UserEntity userEntity) {
        System.out.println("[AuthServiceImplement] signIn");

        Map<String, Object> map = new HashMap<>();

        // 동일한 username 없음
        UserEntity idVerifiedUserEntity = iMemberDaoMapper.isMember(userEntity);

        if (idVerifiedUserEntity != null && passwordEncoder.matches(userEntity.getM_password(), idVerifiedUserEntity.getM_password())) {
            String accessToken = jwtProvider.createAccessToken(userEntity.getEmail(), secretKey);
            String refreshToken = jwtProvider.createRefreshToken(userEntity.getEmail(), secretKey);
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);
            return map;

        } else {
            map.put("result", HttpStatus.NOT_FOUND);
            return map;

        }

    }

    @Override
    public Map<String, Object> refreshToken(HttpServletRequest request, HttpServletResponse response) {
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
        userEmail = jwtProvider.getUserEmail(secretKey, refreshToken);
        if (userEmail != null) {
            if (jwtProvider.validate(secretKey, refreshToken)) {
                log.error("refreshToken이 만료되었습니다.");
            } else {
                String ReAccessToken = jwtProvider.createAccessToken(userEmail, secretKey);
                String ReRefreshToken = jwtProvider.createRefreshToken(userEmail, secretKey);
                map.put("ReAccessToken", ReAccessToken);
                map.put("ReRefreshToken", ReRefreshToken);
                return map;
            }
        }
        map.put("result", HttpStatus.NOT_FOUND);
        return map;
    }
}
