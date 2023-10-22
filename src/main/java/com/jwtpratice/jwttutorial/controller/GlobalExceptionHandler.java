package com.jwtpratice.jwttutorial.controller;

import com.jwtpratice.jwttutorial.common.CustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)

    public String handleCustomException(io.jsonwebtoken.ExpiredJwtException e){
        log.info("handleCustomException");
        return "Token has expired";
    }

}
