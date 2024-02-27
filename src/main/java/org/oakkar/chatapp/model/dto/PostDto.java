package org.oakkar.chatapp.model.dto;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String description;
    @ServerTimestamp
    private Timestamp createdDate;
    @ServerTimestamp
    private Timestamp updatedDate;
    private DocumentReference owner;
}
