package org.oakkar.chatapp.model.record;

import org.oakkar.chatapp.model.enums.RoleName;

import java.time.Instant;

public record UserRecord(long id, String username, String displayName, String email, RoleName roleName, Instant createdDate, Instant updatedDate) {
}
