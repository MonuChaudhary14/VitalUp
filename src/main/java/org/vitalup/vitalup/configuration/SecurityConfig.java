package org.vitalup.vitalup.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

  private final Filter filter;
  private final AuthenticationProvider authenticationProvider;

  public SecurityConfig(Filter filter, AuthenticationProvider authenticationProvider) {
    this.filter = filter;
    this.authenticationProvider = authenticationProvider;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Authorization"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  @Order(1)
  public SecurityFilterChain apiSecurity(HttpSecurity http) {
    http
      .securityMatcher("/api/**") // applies only to API routes
      .cors(withDefaults())
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .requestMatchers("/api/v1/auth/**").permitAll()
        .anyRequest().authenticated()
      )
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
      .formLogin(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .oauth2Login(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain webSecurity(HttpSecurity http) {
    http
      .securityMatcher("/", "/login**", "/oauth2/**", "/dashboard", "/api/user/info")
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/login**", "/oauth2/**").permitAll()
        .anyRequest().authenticated()
      )
      .csrf(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
      )
      .oauth2Login(oauth -> oauth
        .loginPage("/login")
        .defaultSuccessUrl("/dashboard", true)
      );

    return http.build();
  }
}
