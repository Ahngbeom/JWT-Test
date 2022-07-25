package com.ahng.myspringoauth2maven.Config;

import com.ahng.myspringoauth2maven.Component.OAuth2SuccessHandler;
import com.ahng.myspringoauth2maven.JWT.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션을 메소드 단위로 추가하기 위해서 적용
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JWTAccessDeniedHandler jwtAccessDeniedHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2UserService oAuth2UserService;

    // JWT 객체 주입입
    public SecurityConfig(TokenProvider tokenProvider, JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint, JWTAccessDeniedHandler jwtAccessDeniedHandler, OAuth2SuccessHandler oAuth2SuccessHandler, OAuth2UserService oAuth2UserService) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
//                .csrf((c) -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                // 토큰 방식을 채택하기 때문에 csrf 설정 비활성화
                .csrf().disable()

                // 직접 생성한 ExceptionHandling 관련 객체 등록
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler)
                .and()

                // H2 Console을 위한 설정
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()

                // Session을 사용하지 않음으로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정

                // 로그인, 회원가입 API는 토큰이 없는 상태에서 요청이 들어오기 때문에 인증 없이 접근 허용
                .antMatchers(
                        "/api/authenticate",
                        "/api/token/refresh",
                        "/api/user/username-availability",
                        "/api/signup",
                        "/api/logout").permitAll()
                // OAuth2 Login Request URL
                .antMatchers(
                        "/oauth2/**",
                        "/login/oauth2/code/github"
                ).permitAll()

                // Other Unauthenticate URL
                .antMatchers("/", "/js/**", "/webjars/**", "/error/**", "/h2-console/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated() // 나머지 요청들에 대해서는 인증이 필요
                .and()

//                .formLogin().loginPage("/login")
//                .and()

                // JWTFilter를 addFilterBefore로 등록했던 JWTSecurityConfig 클래스 적용
                .apply(new JWTSecurityConfig(tokenProvider))

                .and()
                .oauth2Login();
    }
}
