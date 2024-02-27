package org.oakkar.chatapp.mapper;

import org.oakkar.chatapp.model.dto.UserDto;
import org.oakkar.chatapp.model.entity.User;
import org.oakkar.chatapp.model.payload.request.UserRegistrationRequest;
import org.oakkar.chatapp.model.record.UserRecord;

public interface UserMapper {

    UserRecord mapToRecord(User user);

    UserDto mapToDto(User savedUser);
}
