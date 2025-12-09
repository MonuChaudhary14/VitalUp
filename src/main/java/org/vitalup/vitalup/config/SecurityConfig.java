package org.vitalup.vitalup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/login**", "/oauth2/**").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(oauth -> oauth
        .defaultSuccessUrl("/dashboard", true)
      );

    return http.build();
  }
}
