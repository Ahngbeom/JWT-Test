package com.ahng.myspringoauth2maven.Controller;

import com.ahng.myspringoauth2maven.DTO.User;
import com.ahng.myspringoauth2maven.Service.KakaoService;
import com.ahng.myspringoauth2maven.Service.OAuth2Service;
import com.nimbusds.jose.crypto.impl.HMAC;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.DateUtils;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LogManager.getLogger();

    private final OAuth2Service service;
    private final KakaoService kakaoService;

    private final Environment env;

    @GetMapping("/oauth2/code/{provider}")
    public ResponseEntity<?> oauthLogin(@PathVariable String provider, @RequestParam String code, HttpServletResponse response) {

        User user;

        if (provider.equals("kakao"))
            user = service.verificationKakao(code);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());

        byte[] keyBytes = "AhngBeom".getBytes(StandardCharsets.UTF_8);

        Date expiry = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expiry);
        calendar.add(Calendar.DATE, 1);
        expiry = calendar.getTime();

        String jwtToken = Jwts.builder()
                .setSubject("JwtToken")
                .setExpiration(expiry)
                .setClaims(claims)
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256), SignatureAlgorithm.HS256)
                .compact();

        response.addHeader(env.getProperty("token.header"), env.getProperty("token.prefix") + jwtToken);
        log.warn("JWT: " + jwtToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
