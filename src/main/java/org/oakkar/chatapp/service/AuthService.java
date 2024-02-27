package org.oakkar.chatapp.service;

import org.antlr.v4.runtime.Token;
import org.oakkar.chatapp.model.payload.request.LoginRequest;
import org.oakkar.chatapp.model.payload.request.UserRegistrationRequest;
import org.oakkar.chatapp.model.payload.response.ApiResponse;
import org.oakkar.chatapp.model.payload.response.TokenResponse;
import org.oakkar.chatapp.model.record.UserRecord;

public interface AuthService {
    ApiResponse<?> authenticateUser(LoginRequest loginRequest);

    UserRecord registerUser(UserRegistrationRequest registrationRequest);
}
