package org.oakkar.chatapp.model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String token;
    private String refreshToken;
    private Date expiration;
}
