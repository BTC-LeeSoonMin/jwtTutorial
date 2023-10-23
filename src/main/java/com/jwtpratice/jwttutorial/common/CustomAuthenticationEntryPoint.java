//// CustomException.java
//package com.jwtpratice.jwttutorial.common;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.log4j.Log4j2;
//import org.json.simple.JSONObject;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//import java.io.IOException;
//
//@Log4j2
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException authException)
//            throws IOException, ServletException {
//        String exception = (String) request.getAttribute("exception");
//
//        if (exception == null) {
//            setResponse(response, Code.UNKNOWN_ERROR);
//        }
//        //잘못된 타입의 토큰인 경우
//        else if (exception.equals(Code.WRONG_TYPE_TOKEN.getCode())) {
//            setResponse(response, Code.WRONG_TYPE_TOKEN);
//        }
//        //토큰 만료된 경우
//        else if (exception.equals(Code.EXPIRED_TOKEN.getCode())) {
//            setResponse(response, Code.EXPIRED_TOKEN);
//        }
//        //지원되지 않는 토큰인 경우
//        else if (exception.equals(Code.UNSUPPORTED_TOKEN.getCode())) {
//            setResponse(response, Code.UNSUPPORTED_TOKEN);
//        } else {
//            setResponse(response, Code.ACCESS_DENIED);
//        }
//    }
//
//    //한글 출력을 위해 getWriter() 사용
//    private void setResponse(HttpServletResponse response, Code code) throws IOException {
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        JSONObject responseJson = new JSONObject();
//        responseJson.put("message", code.getMessage());
//        responseJson.put("code", code.getCode());
//
//        response.getWriter().print(responseJson);
//    }
//
//}