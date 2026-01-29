package org.vitalup.vitalup.configuration;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.service.auth.UserNameService;
import org.vitalup.vitalup.service.auth.UserService;

import java.io.IOException;
import java.util.function.Function;

@Component
public class Filter extends OncePerRequestFilter {

  private final UserService userService;
  private final UserNameService usernameService;

  public Filter(UserNameService usernameService, UserService userService) {
    this.usernameService = usernameService;
    this.userService = userService;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {

    String path = request.getRequestURI();
    return path.startsWith("/api/v1/auth/") || "OPTIONS".equalsIgnoreCase(request.getMethod());
  }

  @Override
  protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                  @Nonnull HttpServletResponse response,
                                  @Nonnull FilterChain filterChain)
    throws ServletException, IOException {

    final String header = request.getHeader("Authorization");
    String token = null;
    String userName = null;

    if (header != null && header.startsWith("Bearer ")) {
      token = header.substring(7);
      try {
        userName = usernameService.extractUsername(token);
      } catch (Exception e) {
        filterChain.doFilter(request, response);
        return;
      }
    }

    if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      Users user;

      try {
        if (userName.contains("@")) {
          user = userService.loadUserByEmail(userName);
        } else {
          user = (Users) userService.loadUserByUsername(userName);
        }
      } catch (UsernameNotFoundException e) {
        filterChain.doFilter(request, response);
        return;
      }

      Claims claims = usernameService.extractClaim(token, Function.identity());
      Number version = claims.get("passwordVersion", Number.class);
      int tokenVersion = version != null ? version.intValue() : -1;

      if (usernameService.validToken(token, user) && tokenVersion == user.getPasswordVersion()) {
        UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        System.out.println("Auth set for: " + user.getUsername());
      }
    }

    filterChain.doFilter(request, response);
  }
}
