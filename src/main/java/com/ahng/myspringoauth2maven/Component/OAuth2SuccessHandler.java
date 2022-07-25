package com.ahng.myspringoauth2maven.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
//    private final TokenService tokenService;
//    private final UserRequestMapper userRequestMapper;
//    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("===onAuthenticationSuccess===");

        log.info(authentication.getPrincipal().toString());

//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        log.info("OAuth2User Principal: " + oAuth2User);
//
//        User user = userRequestMapper.toDTO(oAuth2User);
//        log.info("User DTO: " + user);
//
//        Token token = tokenService.generateToken(user.getEmail(), "USER");
//
//        log.info("{}", token);
//
//        writeTokenResponse(response, token);
    }

//    private void writeTokenResponse(HttpServletResponse response, Token token) throws IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        response.addHeader("Auth", token.getToken());
//        response.addHeader("Refresh", token.getRefreshToken());
//        response.setContentType("application/json;charset=UTF-8");
//
//        PrintWriter writer = response.getWriter();
//        writer.println(objectMapper.writeValueAsString(token));
//        writer.flush();
//    }
}
