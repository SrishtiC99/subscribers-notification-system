package com.srishti.auth.service;

import com.srishti.auth.dto.kafka.NewAccountEventDto;
import com.srishti.auth.dto.request.AuthRequest;
import com.srishti.auth.dto.response.TokenResponse;
import com.srishti.auth.entity.User;
import com.srishti.auth.exception.user.UserBadCredentialsException;
import com.srishti.auth.exception.user.UserEmailAlreadyExistsException;
import com.srishti.auth.exception.user.UserNotFoundException;
import com.srishti.auth.mapper.UserMapper;
import com.srishti.auth.model.AccountType;
import com.srishti.auth.model.Role;
import com.srishti.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final KafkaTemplate<String, NewAccountEventDto> template;

    private final String NEW_ACCOUNT_TOPIC = "billing-account-topic";

    private final TokenService tokenService;

    private final JwtService jwtService;

    private final UserMapper mapper;

    public Boolean register(AuthRequest request) {
        return Optional.of(userRepository.findByEmail(request.email()))
                .map(user -> {
                    if (user.isPresent()) {
                        throw new UserEmailAlreadyExistsException("The email provided is already registered in our system.");
                    } else {
                        return request;
                    }
                })
                .map(req -> mapper.mapToEntity(req, passwordEncoder))
                .map(userRepository::saveAndFlush)
                .map(user -> {
                    template.send(NEW_ACCOUNT_TOPIC, NewAccountEventDto.builder()
                            .userId(user.getId())
                            .email(user.getEmail())
                            .accountType(AccountType.USER)
                            .build());
                    return user;
                })
                .isPresent();
    }

    public TokenResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (InternalAuthenticationServiceException e) {
            throw new UserNotFoundException("User ${request.email()} not found");
        } catch (BadCredentialsException e) {
            throw new UserBadCredentialsException("Wrong Password");
        }

        User user = loadUserByUsername(request.email());
        tokenService.deletePreviousClientToken(user);
        String jwt = jwtService.generateJwt(user);
        tokenService.createToken(user, jwt);

        return new TokenResponse(jwt);
    }

    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User ${email} not found"));
    }

    public Boolean updateUserRole(Long userId, Role role) {
        userRepository.findById(userId)
                .map(user -> user.updateRole(role))
                .map(userRepository::saveAndFlush);
        return true;
    }
}
