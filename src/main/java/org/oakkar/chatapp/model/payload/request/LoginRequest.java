package org.oakkar.chatapp.model.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;

    private String password;
}
