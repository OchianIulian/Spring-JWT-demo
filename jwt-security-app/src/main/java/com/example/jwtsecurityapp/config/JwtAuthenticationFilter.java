package com.example.jwtsecurityapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    // This method is used to handle JWT authentication for each incoming request.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Extract the JWT token from the request header.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        // If the Authorization header is missing or doesn't start with "Bearer ", pass the request to the next filter.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7); // Extract the JWT token substring (remove "Bearer " prefix).
        userEmail = jwtService.extractUsername(jwt); // Extract the user email from the JWT token.

        // If the user email is not null and the user is not already authenticated, proceed with authentication.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the UserDetailsService based on the extracted user email.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Check if the JWT token is valid for the user.
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // If the token is valid, create an authentication token for the user and set it in the SecurityContext.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Proceed with the filter chain for the request.
        filterChain.doFilter(request, response);
    }
}