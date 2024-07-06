package edu.usc.csci310.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// This configuration class overrides the default Spring Security configuration so we can display our own web app
// instead of the Spring Security login page
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
                // .requestMatchers("/login", "/signup", "/api/signup", "/api/login", "/index.html", "/", "/static/**", "/api/**",  "/**", "/css/**", "/js/**", "/images/**").permitAll()
                // .anyRequest().authenticated()
            )
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
        return http.build();
    }
}
