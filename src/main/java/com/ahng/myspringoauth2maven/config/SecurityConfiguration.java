package com.ahng.myspringoauth2maven.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
	
	// @Bean
    // public UserDetailsService userDetailsService() {
    //     return new UserDetailsService();
    // }
 
    // @Bean
    // public BCryptPasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests((authz) -> authz
			.antMatchers("/", "/error", "/webjars/**").permitAll()
			.anyRequest().authenticated()
		)
			// .httpBasic(withDefaults())
			.exceptionHandling((e) -> e.
				authenticationEntryPoint(new 
					HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
			)
			.oauth2Login();
		return http.build();
	}

	// @Bean
	// public WebSecurityCustomizer webSecurityCustomizer() {
	// 	return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
	// }
}
