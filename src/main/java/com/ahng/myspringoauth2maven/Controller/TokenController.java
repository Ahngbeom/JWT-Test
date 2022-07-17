package com.ahng.myspringoauth2maven.Controller;

import com.ahng.myspringoauth2maven.JWT.JWTFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class TokenController {

    @GetMapping("/token/refresh")
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(JWTFilter.AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());
            log.info(token);
            return token;
        }
        return null;
    }
}
