package com.jwtpratice.jwttutorial.controller;

import com.jwtpratice.jwttutorial.entity.RefTokenEntity;
import com.jwtpratice.jwttutorial.entity.UserEntity;
import com.jwtpratice.jwttutorial.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign_up")
    public String signUp(@RequestBody Map<String, Object> msgMap, UserEntity userEntity) {
        System.out.println("[AuthController] signUp");
        String response = authService.signUp(msgMap, userEntity);
        return response;
//        return "hi";
    }

    @PostMapping("/sign_in")
    public Object signIn(@RequestBody Map<String, Object> msgMap, UserEntity userEntity, RefTokenEntity refTokenEntity,HttpServletRequest request, HttpServletResponse response) {
        System.out.println("[AuthController] signIn");

        userEntity.setEmail(msgMap.get("email").toString());
        userEntity.setM_password(msgMap.get("m_password").toString());

        Map<String, Object> map = authService.signIn(msgMap, userEntity, refTokenEntity, request,response);

        if (map != null) {
            if (map.get("result") == HttpStatus.NOT_FOUND) {
                return "로그인 아이디가 없거나, 비밀번호를 틀리셨습니다.";
            }

            // 로그인 성공했기때문에 accessToken, refreshToken 발급
            String refreshTokenValue = map.get("refreshToken").toString();
            log.info("refreshTokenValue");
            Cookie cookie = new Cookie("authorization", refreshTokenValue);
            cookie.setHttpOnly(true);  //httponly 옵션 설정
            cookie.setSecure(true); //https 옵션 설정
            cookie.setMaxAge(24 * 3600);
            cookie.setPath("/"); // 모든 곳에서 쿠키열람이 가능하도록 설정
            response.addCookie(cookie);

            return map.get("accessToken");

        } else {
            return null;
        }

    }

    @PostMapping("/refresh_token")
    public Object refreshToken(HttpServletRequest request, HttpServletResponse response, RefTokenEntity refTokenEntity) throws IOException {
        log.info("refreshToken in");

        Map<String, Object> map = authService.refreshToken(request, response, refTokenEntity);
        if(map.get("result") == "nullCheckRefToken"){

            return "이미 탈퇴한 회원 또는 로그아웃이 된 대상이므로 로그인 다시 해주세요.";
        }

        String refreshTokenValue = (String) map.get("ReRefreshToken");
        log.info("ReRefreshToken");
        Cookie cookie = new Cookie("authorization", refreshTokenValue);
        cookie.setHttpOnly(true);  //httponly 옵션 설정
        cookie.setSecure(true); //https 옵션 설정
        cookie.setMaxAge(24 * 3600);
        cookie.setPath("/"); // 모든 곳에서 쿠키열람이 가능하도록 설정
        response.addCookie(cookie);

        return map.get("ReAccessToken");

    }

    @PostMapping("/logout")
    public Object logout(HttpServletRequest request, HttpServletResponse response, RefTokenEntity refTokenEntity) {
        log.info("logout");

        String result = authService.logout(request, response, refTokenEntity);
        if(result == "중복 refToken 삭제 실패"){
            log.info("중복 refToken 삭제 실패");
            return "logout fail";
        }
        if(result == "token 값이 잘못되었습니다."){
            log.info("token 값이 잘못되었습니다.");
            return "token 값이 잘못되었습니다.";
        }

        return "logout success";
    }

    @PostMapping("/sign_out")
    public Object signOut(HttpServletRequest request, HttpServletResponse response,UserEntity userEntity, RefTokenEntity refTokenEntity) {
        log.info("signOut");

        String result = authService.signOut(request, response,userEntity, refTokenEntity);
        if(result == "중복 refToken 삭제 실패"){
            log.info("중복 refToken 삭제 실패");
            return "signOut fail";
        }
        if(result == "token 값이 잘못되었습니다."){
            log.info("token 값이 잘못되었습니다.");
            return "token 값이 잘못되었습니다.";
        }

        return "signOut success";
    }
}
