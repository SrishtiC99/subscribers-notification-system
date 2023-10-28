package com.srishti.auth.service;

import com.srishti.auth.entity.Token;
import com.srishti.auth.entity.User;
import com.srishti.auth.exception.token.InvalidTokenException;
import com.srishti.auth.exception.user.UserJwtNotFoundException;
import com.srishti.auth.exception.user.UserNotFoundException;
import com.srishti.auth.repository.TokenRepository;
import com.srishti.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TokenService {
    private final JwtService jwtService;

    private final AuthService authService;

    private final TokenRepository tokenRepository;

    public void createToken(User user, String jwt) {
        tokenRepository.save(Token.builder().user(user).jwt(jwt).isExpired(false).isRevoked(false).build());
    }

    public void deletePreviousClientToken(User user) {
        tokenRepository.findByUser_Id(user.getId()).ifPresent(tokenRepository::delete);
    }

    public boolean isTokenValid(String jwtToken) {
        User user = takeUserDetailsFromJwt(jwtToken);
        boolean isTokenValid = tokenRepository.findByJwt(jwtToken)
                .map(token -> !token.getIsExpired() && !token.getIsRevoked())
                .orElse(false);

        if (isTokenValid && jwtService.isJwtValid(jwtToken, user)) {
            return true;
        } else {
            throw new InvalidTokenException("Invalid Jwt Token");
        }
    }

    public User takeUserDetailsFromJwt(String jwt) {
        String email = jwtService.extractEmail(jwt);
        try {
            return authService.loadUserByUsername(email);
        } catch (UserNotFoundException e) {
            throw new UserJwtNotFoundException(e.getMessage());
        }
    }
}
