package org.oakkar.chatapp.model.record;

import java.time.Instant;

public record PostRecord(Long id, String description, Instant createdDate, Instant updatedDate, UserRecord userRecord) {
}
