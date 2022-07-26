package com.ahng.myspringoauth2maven.JWT;

import com.ahng.myspringoauth2maven.DTO.TokenDTO;
import com.ahng.myspringoauth2maven.Utils.TokenStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component // TokenProvider Bean 객체 생성
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    private Key key;

    // 의존성 주입
    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.accessToken-validity-in-seconds}") long accessTokenValidityInMilliseconds,
            @Value("${jwt.refreshToken-validity-in-seconds}") long refreshTokenValidityInMilliseconds) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret); // secret 값을 Base64 Decoding 후, key 변수에 할당
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDTO createToken(Authentication authentication) {
        // Authentication 객체의 권한 정보를 이용해서 Token을 생성하는 메소드

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 만료 기한 설정
        long now = (new Date()).getTime();
        Date accessTokenValidity = new Date(now + this.accessTokenValidityInMilliseconds);
        Date refreshTokenValidity = new Date(now + this.refreshTokenValidityInMilliseconds);

        logger.info("Access Token Validity: " + accessTokenValidity);

        // JWT 생성 및 반환
        return new TokenDTO(
                Jwts.builder()
                        .setSubject(authentication.getName())
                        .claim(AUTHORITIES_KEY, authorities)
                        .signWith(key, SignatureAlgorithm.HS512)
                        .setExpiration(accessTokenValidity)
                        .compact(),
                Jwts.builder()
                        .setSubject(authentication.getName())
                        .claim(AUTHORITIES_KEY, authorities)
                        .signWith(key, SignatureAlgorithm.HS512)
                        .setExpiration(refreshTokenValidity)
                        .compact());
    }

    public Authentication getAuthentication(String token) {
        // Token에 담겨있는 정보를 이용해 Authentication 객체를 리턴하는 메소드

        // Token을 통해 Claims 객체 생성
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 권한 정보를 이용해서 User 객체 생성
        User principal = new User(claims.getSubject(), "", authorities);

        //User 객체와 token, 권한 정보를 이용해 Authentication 객체 생성 및 반환
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // Token의 유효성 검사 메소드
    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 토큰 파싱 시도
            logger.info("유효한 JWT 토큰입니다.");
            return TokenStatus.VALID_ACCESS_TOKEN;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.error("잘못된 JWT 서명입니다.");
            return TokenStatus.WRONG_SIGNATURE;
        } catch (ExpiredJwtException e) {
            logger.warn("만료된 JWT 토큰입니다.");
            return TokenStatus.EXPIRED_ACCESS_TOKEN;
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 JWT 토큰입니다.");
            return TokenStatus.NOT_SUPPORTED_ACCESS_TOKEN;
        } catch (IllegalArgumentException e) {
            logger.error("잘못된 JWT 토큰입니다.");
            return TokenStatus.WRONG_ACCESS_TOKEN;
        } catch (Exception e) {
            logger.error("유효하지 않은 JWT 토큰입니다.");
            return TokenStatus.INVALID_ACCESS_TOKEN;
        }
    }

    public LocalDateTime getTokenExpiryTime(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return claims.getExpiration().toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

}
