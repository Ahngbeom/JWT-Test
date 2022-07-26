package com.ahng.myspringoauth2maven.Component;

import com.ahng.myspringoauth2maven.DTO.OAuth2UserDTO;
import com.ahng.myspringoauth2maven.DTO.TokenDTO;
import com.ahng.myspringoauth2maven.DTO.UserDTO;
import com.ahng.myspringoauth2maven.JWT.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("===onAuthenticationSuccess===");

        log.info(authentication.getPrincipal().toString());

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("OAuth2User Principal: " + oAuth2User);

        OAuth2UserDTO user = userRequestMapper.toDto(oAuth2User);
        log.info("User DTO: " + user);

        TokenDTO token = tokenProvider.createToken(authentication);

        log.info("{}", token);

        writeTokenResponse(response, token);
    }

    private void writeTokenResponse(HttpServletResponse response, TokenDTO token) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Auth", token.getAccessToken());
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}
