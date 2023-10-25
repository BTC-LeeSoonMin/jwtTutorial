package com.jwtpratice.jwttutorial.config;

//import com.jwtpratice.jwttutorial.common.CustomAuthenticationEntryPoint;
import com.jwtpratice.jwttutorial.filter.JwtAuthenticationFilter;
import com.jwtpratice.jwttutorial.filter.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;


    // throws Exception은 모든 Exception 처리를 호출부에서 하겠다라는 의미
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors().and()
                .csrf().disable() // 공격자가 사용자의 의지 없이(모르게) 공격하게 만드는 보안 공격
                .httpBasic().disable() // disable 안하면 security 로그인창 뜸
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter,JwtAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers("/","/api/sign_up","/api/sign_in", "/api/refresh_token", "/api/logout","/api/sign_out").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/**").permitAll() // 모든 Get 요청은 인증하지 않겠다
                .anyRequest().authenticated()
                .and().build();

    }




}
