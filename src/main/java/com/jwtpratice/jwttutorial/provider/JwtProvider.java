package com.jwtpratice.jwttutorial.provider;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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






}
