package com.ahng.myspringoauth2maven.JWT;

import com.ahng.myspringoauth2maven.Utils.TokenStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        // JWT의 인증정보를 현재 실행 중인 SecurityContext에 저장하는 역할 수행

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        String accessToken = resolveAccessToken(httpServletRequest); // HttpServletRequest에서 토큰을 얻어낸다.
        String refreshToken = resolveRefreshToken(httpServletRequest); // HttpServletRequest에서 토큰을 얻어낸다.

        if (requestURI.equals("/api/authenticate") && SecurityContextHolder.getContext().getAuthentication() != null) {
            logger.error("이미 로그인 상태입니다. 로그아웃 후 진행해주세요");
        } else if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken).equals(TokenStatus.VALID_ACCESS_TOKEN)) { // 얻어온 토큰 유효성 검사
            Authentication authentication = tokenProvider.getAuthentication(accessToken); // 유효한 토큰이라면 토큰에 담겨져있는 Authentication(인증 정보)를 얻어낸다.
            SecurityContextHolder.getContext().setAuthentication(authentication); // 얻어낸 Authentication 객체를 SecurityContext에 저장
            logger.info("Security Context에 '{}' 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
            logger.info("Access token: " + accessToken);
            logger.info("Access token validity: " + tokenProvider.getExpirationToken(accessToken) + ")");
            logger.info("Refresh token: " + refreshToken);
            logger.info("Refresh token validity: " + tokenProvider.getExpirationToken(refreshToken) + ")");
        } else {
            if (requestURI.startsWith("/api/"))
                logger.error("유효한 JWT 토큰이 없습니다. uri: {}", requestURI);
        }

        chain.doFilter(servletRequest, servletResponse); // 다른 필터에게 Servlet Request, Servlet Response 객체 전달
    }

    // RequestHeader에서 토큰 정보를 꺼내오기 위한 resolveToken 메소드
    private String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("refresh_token"))
                    return c.getValue();
            }
        }
        return null;
    }
}
