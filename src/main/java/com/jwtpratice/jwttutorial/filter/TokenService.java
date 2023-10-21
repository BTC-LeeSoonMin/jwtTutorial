//package com.jwtpratice.jwttutorial.filter;
//
//import com.jwtpratice.jwttutorial.provider.JwtProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TokenService {
//
//    @Autowired
//    private JwtProvider jwtProvider;
//
//    public String validateAndGenerateToken(String accessToken, String refreshToken) {
//        // Access Token 검증
//        String userId = jwtProvider.validate(accessToken);
//
//        // Access Token이 만료된 경우
//        if (userId == null) {
//            // Refresh Token 검증
//            String refreshedUserId = jwtProvider.validate(refreshToken);
//
//            // Refresh Token도 만료된 경우
//            if (refreshedUserId == null) {
//                // 적절한 오류 메시지나 예외 처리
//                throw new IllegalStateException("Both access token and refresh token are expired or invalid.");
//            }
//
//            // 새로운 Access Token 발급
//            String newAccessToken = jwtProvider.createAccessToken(refreshedUserId);
//
//            // 새로운 Refresh Token 발급
//            String newRefreshToken = jwtProvider.createRefreshToken(refreshedUserId);
//
//            // 새로운 토큰 반환
//            return "New Access Token: " + newAccessToken + ", New Refresh Token: " + newRefreshToken;
//        }
//
//        // Access Token이 유효한 경우
//        return "Access Token is valid.";
//    }
//
//}
