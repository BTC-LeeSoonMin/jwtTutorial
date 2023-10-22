// CustomException.java
package com.jwtpratice.jwttutorial.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

@Log4j2
public class CustomException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        log.info("msg : "+message);
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
