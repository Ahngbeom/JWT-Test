package com.ahng.myspringoauth2maven.Controller;

import com.ahng.myspringoauth2maven.DTO.LoginDTO;
import com.ahng.myspringoauth2maven.DTO.TokenDTO;
import com.ahng.myspringoauth2maven.JWT.JWTFilter;
import com.ahng.myspringoauth2maven.JWT.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

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
    public ResponseEntity<TokenDTO> authorize(@Valid @RequestBody LoginDTO loginDTO) {
        // LoginDTO 객체의 정보를 기준으로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        // 얻어낸 토큰을 통해서 authenticate 메소드 실행 시 CustomUserDetailsService 클래스의 loadUserByUsername 메소드가 연쇄적으로 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Authentication 객체 타입의 유저 정보 결과 값을 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Authentication(인증 정보)를 기준으로 JWT 생성
        String jwt = tokenProvider.createToken(authentication);

        // 얻어낸 토큰(JWT)을 ResponseHeader에 저장
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // TokenDTO 객체에 토큰을 저장하고, ResponseBody에 TokenDTO 객체를 담아준 후 반환 
        return new ResponseEntity<>(new TokenDTO(jwt), httpHeaders, HttpStatus.OK);
    }

}
