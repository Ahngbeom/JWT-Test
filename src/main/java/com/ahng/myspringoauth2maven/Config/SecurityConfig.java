package com.ahng.myspringoauth2maven.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

// @EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests((auth) -> auth
                        .antMatchers("/", "/error", "/webjars/**", "/h2-console/**", "/h2/**", "/user/**").permitAll()
                        .anyRequest().authenticated() // 어떠한 요청이든 권한이 필요하게된다.
                )
                .exceptionHandling((e) -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .csrf().disable()
//                .csrf((c) -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringAntMatchers("/h2-console/**", "/h2/**"))
//                .headers().frameOptions().sameOrigin()
//                .and()
                .logout((l) -> l.logoutSuccessUrl("/").permitAll())
                .oauth2Login();
        return http.build();
    }

}
