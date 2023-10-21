package com.jwtpratice.jwttutorial.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api")
public class HelloController {

    @PostMapping("/hello")
    public ResponseEntity<String> hello() {
        log.info("hello()");
        return ResponseEntity.ok("hello");
    }

}
