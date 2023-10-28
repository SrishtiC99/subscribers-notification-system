package com.srishti.auth.service;

import com.srishti.auth.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class LogoutService implements LogoutHandler {
    private final TokenService tokenService;

    private final AuthService authService;

    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String jwt = jwtService.extractJwt(request);

        if (jwt != null && !jwt.isEmpty()) {
            String email = jwtService.extractEmail(jwt);
            User user = authService.loadUserByUsername(email);

            tokenService.deletePreviousClientToken(user);
            SecurityContextHolder.clearContext();
        }
    }
}
