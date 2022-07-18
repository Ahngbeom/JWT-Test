package com.ahng.myspringoauth2maven.JWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class JWTFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";
//    public static final String ACCESS_TOKEN_HEADER = "Access-Token";
//    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // JWT의 인증정보를 현재 실행 중인 SecurityContext에 저장하는 역할 수행
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        String jwt = resolveToken(httpServletRequest); // HttpServletRequest에서 토큰을 얻어낸다.

        if (requestURI.equals("/api/authenticate") && SecurityContextHolder.getContext().getAuthentication() != null) {
            logger.error("이미 로그인 상태입니다. 로그아웃 후 진행해주세요");
        } else if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // 얻어온 토큰 유효성 검사
            Authentication authentication = tokenProvider.getAuthentication(jwt); // 유효한 토큰이라면 토큰에 담겨져있는 Authentication(인증 정보)를 얻어낸다.
            SecurityContextHolder.getContext().setAuthentication(authentication); // 얻어낸 Authentication 객체를 SecurityContext에 저장
            logger.info("Security Context에 '{}' 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
        } else {
            logger.error("유효한 JWT 토큰이 없습니다. uri: {}", requestURI);
        }
        
        chain.doFilter(servletRequest, servletResponse); // 다른 필터에게 Servlet Request, Servlet Response 객체 전달
    }

    // RequestHeader에서 토큰 정보를 꺼내오기 위한 resolveToken 메소드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
}
