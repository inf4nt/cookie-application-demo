package com.cookie_app_demo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping
@SpringBootApplication
public class CookieDemoApplication {

    private static final Logger log = LoggerFactory.getLogger(CookieDemoApplication.class);

    private static final String COOKIE_NAME = "cookiedemoapp";

    public static void main(String[] args) {
        SpringApplication.run(CookieDemoApplication.class, args);
    }

    @GetMapping
    public ResponseEntity<?> index() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        Cookie cookie = new Cookie(COOKIE_NAME, "cookieValue123");
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");

        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.SET_COOKIE,
                cookie.getName() + "=" + cookie.getValue() + "; Max-Age=" + cookie.getMaxAge() + "; Path=/"
        );

        return new ResponseEntity<>("Cookie is set!", headers, HttpStatus.OK);
    }

    @GetMapping("/save")
    public ResponseEntity<String> saveCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    log.info("Cookie is OK. Key ={} Value ={} ", cookie.getName(), cookie.getValue());
                    return new ResponseEntity<>("Cookie received: " + cookie.getValue(), HttpStatus.OK);
                }
            }
        }
        log.info("No cookie found ={}", cookies);
        return new ResponseEntity<>("No cookie found", HttpStatus.BAD_REQUEST);
    }
}
