package com.srishti.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srishti.auth.dto.response.ErrorResponse;
import com.srishti.auth.service.JwtService;
import com.srishti.auth.service.TokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = jwtService.extractJwt(request);

        if (jwt != null && !jwt.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = tokenService.takeUserDetailsFromJwt(jwt);
                if (tokenService.isTokenValid(jwt)) {
                    UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(userDetails, request);
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(context);
                }
            } catch (JwtException e) {
                handleInvalidJwtException(response, e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    private void handleInvalidJwtException(HttpServletResponse response, String message) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .description("Invalid JWT")
                .code(HttpStatus.FORBIDDEN.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
