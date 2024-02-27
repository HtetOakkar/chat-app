package org.oakkar.chatapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.oakkar.chatapp.model.payload.request.LoginRequest;
import org.oakkar.chatapp.model.payload.request.UserRegistrationRequest;
import org.oakkar.chatapp.model.payload.response.ApiResponse;
import org.oakkar.chatapp.model.record.UserRecord;
import org.oakkar.chatapp.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ApiResponse<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/registration")
    public ApiResponse<?> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        UserRecord userRecord = authService.registerUser(registrationRequest);
        return ApiResponse.success(userRecord, "Registration successful.");
    }
}
