package org.oakkar.chatapp.model.dto;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oakkar.chatapp.model.enums.RoleName;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String displayName;

    private String password;

    @ServerTimestamp
    private Timestamp createDate;

    @ServerTimestamp
    private Timestamp updateDate;

    private long version;

    private RoleName roleName;

    private DocumentReference refreshToken;
}
