package com.jwtpratice.jwttutorial.filter;

import com.jwtpratice.jwttutorial.provider.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // @RequiredArgsConstructor을 이용하면 final로 지정된 것은 필수 생성자로 여긴다
    private final JwtProvider jwtProvider;
//    private final TokenService tokenService;

    @Value("${secret-key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info(authorization);

        // token안보내면 Block
//        if (authorization == null) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("authorization이 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.substring(7);
        System.out.println("token~ : " + token);

        // token 만료되었는지 확인


        if (JwtProvider.validate(secretKey, token)) {
            log.error("token이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // getUserEmail
        String email = JwtProvider.getUserEmail(secretKey, token);
        System.out.println("email~ : " + email);

        AbstractAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.NO_AUTHORITIES);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 인증 요청에 대한 세부정보 작성 가능

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);

        SecurityContextHolder.setContext(securityContext);


        filterChain.doFilter(request, response); // 다음 필터로

    }


}

//    private String parseBearerToken(HttpServletRequest request) {
//        System.out.println("parseBearerToken");
//        String authorization = request.getHeader("Authorization");
//
//        boolean hasAuthorization = StringUtils.hasText(authorization); // hasText => null, 길이가 0, 공백이라면 false 반환
//        if (!hasAuthorization) return null;
//        boolean isBearer = authorization.startsWith("Bearer "); // Bearer문자열 이후 한칸 띄운건지 확인
//        if (!isBearer) return null;
//
//        String token = authorization.substring(7);
//        log.info("token~ {}", token);
//        return token;
//
//    }
