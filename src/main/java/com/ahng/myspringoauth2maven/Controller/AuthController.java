package com.ahng.myspringoauth2maven.Controller;

import com.ahng.myspringoauth2maven.DTO.LoginDTO;
import com.ahng.myspringoauth2maven.DTO.TokenDTO;
import com.ahng.myspringoauth2maven.Enumeration.TokenType;
import com.ahng.myspringoauth2maven.JWT.JWTFilter;
import com.ahng.myspringoauth2maven.JWT.TokenProvider;
import com.ahng.myspringoauth2maven.Utils.SecurityUtil;
import com.ahng.myspringoauth2maven.Utils.TokenStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    // 로그인 API
    @PostMapping("/authenticate")
    public ResponseEntity<?> authorize(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody LoginDTO loginDTO) throws RuntimeException {
        try {
            String authorizationHeader = request.getHeader(JWTFilter.AUTHORIZATION_HEADER);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && !authorizationHeader.substring("Bearer ".length()).equals("null")) {
                throw new RuntimeException("Already Logged In");
            }

            // LoginDTO 객체의 정보를 기준으로 UsernamePasswordAuthenticationToken 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

            log.warn(authenticationToken.getName());

            // 얻어낸 토큰을 통해서 authenticate 메소드 실행 시 CustomUserDetailsService 클래스의 loadUserByUsername 메소드가 연쇄적으로 실행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            log.warn(String.valueOf(authentication.getPrincipal()));

            // Authentication 객체 타입의 유저 정보 결과 값을 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Held Security Contenxt: " + authentication.getPrincipal() + ", " + SecurityUtil.getCurrentUsername());

            // Authentication(인증 정보)를 기준으로 JWT 생성
            TokenDTO tokenDTO = tokenProvider.createToken(authentication);

            // 얻어낸 토큰(JWT)을 ResponseHeader에 저장
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDTO.getAccessToken());

            Objects.requireNonNull(httpHeaders.get(JWTFilter.AUTHORIZATION_HEADER)).forEach(value -> log.info(JWTFilter.AUTHORIZATION_HEADER + ": " + value));

            Cookie refreshTokenCookie = new Cookie("refresh_token", tokenDTO.getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            response.addCookie(refreshTokenCookie);
            log.info("Refresh Token: " + tokenDTO.getRefreshToken());

            // TokenDTO 객체에 토큰을 저장하고, ResponseBody에 TokenDTO 객체를 담아준 후 반환
            return new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication;

        HttpHeaders httpHeaders = new HttpHeaders();

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    if (tokenProvider.validateToken(cookie.getValue()).equals(TokenStatus.VALID_ACCESS_TOKEN)) {
                        authentication = tokenProvider.getAuthentication(cookie.getValue());
                        TokenDTO tokenDTO = tokenProvider.createToken(authentication);
                        cookie.setValue(tokenDTO.getRefreshToken());
                        cookie.setHttpOnly(true);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        httpHeaders.set(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDTO.getAccessToken());
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        log.info("=== Refresh Token ===");
                        log.info("Authentication: " + authentication.getPrincipal() + ", " + SecurityUtil.getCurrentUsername());
                        log.info("Access token: " + tokenDTO.getAccessToken());
                        log.info("Access token validity: " + tokenProvider.getExpirationToken(tokenDTO.getAccessToken()).toString() + ")");
                        log.info("Refresh token: " + tokenDTO.getRefreshToken());
                        log.info("Refresh token validity: " + tokenProvider.getExpirationToken(tokenDTO.getRefreshToken()).toString() + ")");

                        return new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Expired Refresh Token", httpHeaders, HttpStatus.NOT_FOUND);
                    }
                }
            }
        }
        return new ResponseEntity<>("Not found is Refresh Token", httpHeaders, HttpStatus.NOT_FOUND);
    }

}
