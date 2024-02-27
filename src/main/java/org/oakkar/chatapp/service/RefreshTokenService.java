package org.oakkar.chatapp.service;

public interface RefreshTokenService {
    String generateRefreshToken(long userId);
}
