package com.jwtpratice.jwttutorial.provider;

import com.jwtpratice.jwttutorial.common.CustomException;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Log4j2
@Component
public class JwtProvider {

    @Value("${accessToken-valid-seconds}")
    private Long accessTokenExpireTimeMs;
    @Value("${refreshToken-valid-seconds}")
    private Long RefreshTokenExpireTimeMs;

    public String createAccessToken(String email, String key) {
        Claims claims = Jwts.claims(); // 일종의 map
        claims.put("email",  email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (accessTokenExpireTimeMs * 1000)))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

    }
    public String createRefreshToken(String email, String key) {
        Claims claims = Jwts.claims(); // 일종의 map
        claims.put("email",  email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (RefreshTokenExpireTimeMs * 1000)))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

    }

    // Claims에서 login email 꺼내기
    public static String getUserEmail(String secretKey, String token ) {
        log.info("getUserEmail in");
        return extractClaims(secretKey, token).get("email").toString();
    }
    // 밝급된 Token이 만료 시간이 지났는지 체크
    public static boolean validate(String secretKey, String token) {
        log.info("validate in");
        Date expiredDate = extractClaims(secretKey,token).getExpiration();
        // Token의 만료 날짜가 지금보다 이전인지 check
        return expiredDate.before(new Date());
    }
//     SecretKey를 사용해 Token Parsing
    private static Claims extractClaims(String secretKey, String token) {
        log.info("extractClaims in");
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
    // SecretKey를 사용해 Token Parsing
//    private static Claims extractClaims(String secretKey, String token) {
//        log.info("extractClaims in");
//        try {
//            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//        } catch (ExpiredJwtException e) {
//            log.info("Token expired");
//            throw new CustomException("Token expired", HttpStatus.UNAUTHORIZED);
//        } catch (JwtException e) {
//            log.info("Invalid token");
//            throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED);
//        }
//    }


}
