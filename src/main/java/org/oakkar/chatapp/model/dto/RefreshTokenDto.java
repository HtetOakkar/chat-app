package org.oakkar.chatapp.model.dto;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenDto {
    private long id;

    private String refreshToken;

    @ServerTimestamp
    private Timestamp createdDate;

    @ServerTimestamp
    private Timestamp updatedDate;
}
