package org.oakkar.chatapp.service.impl;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.oakkar.chatapp.mapper.UserMapper;
import org.oakkar.chatapp.model.dto.RefreshTokenDto;
import org.oakkar.chatapp.model.entity.RefreshToken;
import org.oakkar.chatapp.model.entity.User;
import org.oakkar.chatapp.model.exception.ResourceNotFoundException;
import org.oakkar.chatapp.repository.RefreshTokenRepository;
import org.oakkar.chatapp.repository.UserRepository;
import org.oakkar.chatapp.service.RefreshTokenService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final Firestore firestore;

    private final UserMapper userMapper;

    @Override
    public String generateRefreshToken(long userId) {
        String token = UUID.randomUUID().toString();

        Thread thread = new Thread(() -> {
            User user = userRepository.findById(userId).
                    orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with Id : '%s'", userId)));
            RefreshToken refreshToken = user.getRefreshToken() != null? user.getRefreshToken(): new RefreshToken();
            refreshToken.setRefreshToken(token);
            refreshToken.setUser(user);
            RefreshToken savedRefreshToken =  refreshTokenRepository.save(refreshToken);
            user.setRefreshToken(savedRefreshToken);
            User savedUser = userRepository.save(user);
            DocumentReference refreshTokenRef = firestore.collection("refresh_tokens").document(savedRefreshToken.getId().toString());
            refreshTokenRef.set(RefreshTokenDto.builder()
                            .id(savedRefreshToken.getId())
                            .refreshToken(savedRefreshToken.getRefreshToken())
                            .createdDate(Timestamp.ofTimeMicroseconds(savedRefreshToken.getCreatedDate().toEpochMilli()))
                            .updatedDate(Timestamp.ofTimeMicroseconds(savedRefreshToken.getUpdatedDate().toEpochMilli()))
                            .build());
            DocumentReference userRef = firestore.collection("users").document(savedUser.getId().toString());
            userRef.set(userMapper.mapToDto(savedUser));
        });
        thread.start();
        return token;
    }
}
