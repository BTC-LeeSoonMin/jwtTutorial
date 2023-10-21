package com.jwtpratice.jwttutorial.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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



    public static boolean validate( String secretKey, String token) {

        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }

//    public static boolean validate(String secretKey,String token){
//        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token)
//                .getBody().getExpiration().before(new Date());
//    }

    public static String getUserEmail(String secretKey,String token){
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJwt(token)
                .getBody().get("userName", String.class);
    }


}
