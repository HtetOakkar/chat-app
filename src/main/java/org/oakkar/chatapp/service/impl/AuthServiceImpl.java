package org.oakkar.chatapp.service.impl;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.oakkar.chatapp.mapper.UserMapper;
import org.oakkar.chatapp.model.entity.User;
import org.oakkar.chatapp.model.exception.PasswordMismatchException;
import org.oakkar.chatapp.model.payload.request.LoginRequest;
import org.oakkar.chatapp.model.payload.request.UserRegistrationRequest;
import org.oakkar.chatapp.model.payload.response.ApiResponse;
import org.oakkar.chatapp.model.payload.response.ErrorResponse;
import org.oakkar.chatapp.model.payload.response.TokenResponse;
import org.oakkar.chatapp.model.record.UserRecord;
import org.oakkar.chatapp.repository.UserRepository;
import org.oakkar.chatapp.security.jwt.JwtProvider;
import org.oakkar.chatapp.security.principal.UserPrincipal;
import org.oakkar.chatapp.service.AuthService;
import org.oakkar.chatapp.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;

    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final Firestore firestore;

    @Override
    public ApiResponse<?> authenticateUser(LoginRequest loginRequest) {

        try {
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                Date expiration = new Date(new Date().getTime() + 86400000);
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String token = jwtProvider.generateToken(authentication);
                String refreshToken = refreshTokenService.generateRefreshToken(userPrincipal.getId());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return ApiResponse.success(new TokenResponse(token, refreshToken, expiration), "Login successful.");
            }
        } catch (BadCredentialsException e) {
            throw new PasswordMismatchException("Incorrect Password");
        }

        return ApiResponse.error(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, "You are not authorized."), "Username or Password Incorrect.");

    }

    @Override
    public UserRecord registerUser(UserRegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setDisplayName(registrationRequest.getDisplayName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());
        User savedUser = userRepository.save(user);

        firestore.collection("users").document(savedUser.getId().toString()).set(userMapper.mapToDto(savedUser));
        return userMapper.mapToRecord(savedUser);
    }
}
