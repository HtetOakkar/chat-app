package org.oakkar.chatapp.mapper.impl;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.oakkar.chatapp.mapper.PostMapper;
import org.oakkar.chatapp.model.dto.PostDto;
import org.oakkar.chatapp.model.dto.UserDto;
import org.oakkar.chatapp.model.entity.Post;
import org.oakkar.chatapp.model.record.PostRecord;
import org.oakkar.chatapp.model.record.UserRecord;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class PostMapperImpl implements PostMapper {

    private final Firestore firestore;
    @Override
    public PostRecord mapToRecord(Post savedPost) {

        return savedPost != null ?
                new PostRecord(savedPost.getId(),
                        savedPost.getDescription(),
                        savedPost.getCreatedDate(),
                        savedPost.getUpdatedDate(),
                        new UserRecord(savedPost.getUser().getId(),
                                savedPost.getUser().getUsername(),
                                savedPost.getUser().getDisplayName(),
                                savedPost.getUser().getEmail(),
                                savedPost.getUser().getRoleName(),
                                savedPost.getUser().getCreateDate(),
                                savedPost.getUser().getUpdateDate())): null;
    }

    @Override
    public PostDto mapToDto(Post savedPost) {

        DocumentReference userDocRef = firestore.collection("users").document(savedPost.getUser().getId().toString());
        return PostDto.builder()
                .id(savedPost.getId())
                .description(savedPost.getDescription())
                .createdDate(Timestamp.ofTimeMicroseconds(savedPost.getCreatedDate().toEpochMilli()))
                .updatedDate(Timestamp.ofTimeMicroseconds(savedPost.getUpdatedDate().toEpochMilli()))
                .owner(userDocRef)
                .build();
    }

    @Override
    public PostRecord maptoRecord(PostDto postDto) {
        DocumentReference userDocRef = postDto.getOwner();
        UserDto userDto;
        try {
            userDto = userDocRef.get().get().toObject(UserDto.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return new PostRecord(
                postDto.getId(),
                postDto.getDescription(),
                postDto.getCreatedDate().toDate().toInstant(),
                postDto.getUpdatedDate().toDate().toInstant(),
                userDto != null?
                        new UserRecord(
                                userDto.getId(),
                                userDto.getUsername(),
                                userDto.getDisplayName(),
                                userDto.getEmail(),
                                userDto.getRoleName(),
                                userDto.getCreateDate().toDate().toInstant(),
                                userDto.getUpdateDate().toDate().toInstant()
                        ) : null
        );
    }
}
