package org.oakkar.chatapp.mapper.impl;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.oakkar.chatapp.mapper.UserMapper;
import org.oakkar.chatapp.model.dto.UserDto;
import org.oakkar.chatapp.model.entity.User;
import org.oakkar.chatapp.model.record.UserRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final Firestore firestore;

    @Override
    public UserDto mapToDto(User savedUser) {


        DocumentReference refreshTokenRef = savedUser.getRefreshToken() != null?
                firestore.collection("refresh_tokens").document(savedUser.getRefreshToken().getId().toString()) : null;

        return UserDto.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .displayName(savedUser.getDisplayName())
                .username(savedUser.getUsername())
                .password(savedUser.getPassword())
                .version(savedUser.getVersion())
                .createDate(Timestamp.ofTimeMicroseconds(savedUser.getCreateDate().toEpochMilli()))
                .updateDate(Timestamp.ofTimeMicroseconds(savedUser.getUpdateDate().toEpochMilli()))
                .roleName(savedUser.getRoleName())
                .refreshToken(refreshTokenRef)
                .build();
    }

    @Override
    public UserRecord mapToRecord(User user) {
        return user == null ? null : new UserRecord(user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getRoleName(),
                user.getCreateDate(),
                user.getUpdateDate());
    }
}
