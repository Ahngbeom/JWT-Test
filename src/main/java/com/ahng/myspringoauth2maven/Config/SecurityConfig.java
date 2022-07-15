package com.ahng.myspringoauth2maven.Config;

import com.ahng.myspringoauth2maven.Component.OAuth2SuccessHandler;
import com.ahng.myspringoauth2maven.Service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

// @EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests((auth) -> auth
                        .antMatchers("/", "/error", "/webjars/**", "/h2-console/**", "/h2/**", "/user/**", "/oauth/**", "/oauth2/**").permitAll()
                        .anyRequest().authenticated() // 어떠한 요청이든 권한이 필요하게된다.
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling((e) -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .csrf().disable() // csrf 비활성화
//                .csrf((c) -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringAntMatchers("/h2-console/**", "/h2/**"))
//                .headers().frameOptions().sameOrigin()
//                .and()
                .logout((l) -> l.logoutSuccessUrl("/").permitAll()) // Logout 성공 후 Redirection URL 지정 및 해당 URL 모두에게 접근 허용
//                .oauth2Login();
                .oauth2Login().successHandler(successHandler).userInfoEndpoint().userService(oAuth2UserService);
        return http.build();
    }

}
