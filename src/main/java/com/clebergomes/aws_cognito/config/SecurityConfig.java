package com.clebergomes.aws_cognito.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(
        requests -> requests
            .requestMatchers("/admin")
            .hasAnyRole("Admin", "Editor")
            .requestMatchers("/login")
            .permitAll()
            .requestMatchers("/register")
            .permitAll()
            .requestMatchers("/logout")
            .permitAll()
            .anyRequest()
            .authenticated());

    http.csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }
}
