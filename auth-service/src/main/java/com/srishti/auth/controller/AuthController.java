package com.srishti.auth.controller;

import com.srishti.auth.dto.request.AuthRequest;
import com.srishti.auth.dto.response.TokenResponse;
import com.srishti.auth.entity.User;
import com.srishti.auth.service.AuthService;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Observed(name = "New User Signup")
    @PostMapping("/register")
    public ResponseEntity<Boolean> register(
            @RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Observed(name = "User Login")
    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponse> authenticate(
            @RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @Observed(name = "Token Validation")
    @GetMapping("/validate")
    public ResponseEntity<Map<Long, String>> isTokenValid(
            @AuthenticationPrincipal User user) {
        System.out.println("Api gateway validating request: " + user.getEmail());
        return ResponseEntity.ok(Map.of(user.getId(), user.getRole().getAuthority()));
    }
}
