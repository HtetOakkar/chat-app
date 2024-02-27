package org.oakkar.chatapp.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest {
    @NotBlank(message = "Username cannot be blank.")
    private String username;
    @JsonProperty("display_name")
    @NotBlank(message = "Display name cannot be blank")
    private String displayName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank.")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 32, message = "Password must be between 8 to 32 characters")
    private String password;
}
