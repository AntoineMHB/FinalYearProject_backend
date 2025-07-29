package com.antoine.springJwt.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.antoine.springJwt.service.JwtService;
import com.antoine.springJwt.service.UserDetailsServiceImp;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImp userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImp userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/auth/login")
                || path.startsWith("/register")
                || path.startsWith("/forgotPassword")
                || path.startsWith("/reset-password")
                || path.startsWith("/api/audit-logs");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

            if (isPublicEndpoint(request)) {
                filterChain.doFilter(request, response);
                return;
            }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("Extracted JWT Token: " + token);  // Log the token

        String email = jwtService.extractUserEmail(token);
        if (email != null) {
            System.out.println("Extracted email from token: " + email);  // Log the email

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (userDetails != null) {
                    System.out.println("User details loaded for email: " + email);  // Log successful user loading

                    if (jwtService.isValid(token, userDetails)) {
                        System.out.println("JWT is valid, authenticating user...");  // Log token validity

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        System.out.println("Invalid JWT token.");
                    }
                } else {
                    System.out.println("User not found for email: " + email);  // Log user not found
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
