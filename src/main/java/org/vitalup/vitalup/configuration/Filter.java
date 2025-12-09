package org.vitalup.vitalup.configuration;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vitalup.vitalup.entities.Auth.Users;
import org.vitalup.vitalup.service.AuthService.UserNameService;
import org.vitalup.vitalup.service.AuthService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

@Component
public class Filter extends OncePerRequestFilter{

    private final UserService service;
    private final UserNameService usernameService;

    public Filter(UserNameService usernameService, UserService service) {
        this.usernameService = usernameService;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException{

        final String header =request.getHeader("Authorization");
        String token =null;
        String userName = null;

        if(header != null && header.startsWith("Bearer ")){
            token = header.substring(7);
            userName = usernameService.extractUsername(token);
        }

        if(userName != null || SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = service.loadUserByUsername(userName);
            Users user = (Users) userDetails;

            Claims claims = usernameService.extractClaim(token, Function.identity());
            int tokenVersion = (Integer) claims.get("passwordVersion");

            if(usernameService.validToken(token, user) && tokenVersion == user.getPasswordVersion()){
                UsernamePasswordAuthenticationToken authToken =new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        filterChain.doFilter(request, response);
    }


}
