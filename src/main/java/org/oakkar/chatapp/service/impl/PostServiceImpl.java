package org.oakkar.chatapp.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oakkar.chatapp.mapper.PostMapper;
import org.oakkar.chatapp.model.dto.PostDto;
import org.oakkar.chatapp.model.entity.Post;
import org.oakkar.chatapp.model.entity.User;
import org.oakkar.chatapp.model.exception.ResourceNotFoundException;
import org.oakkar.chatapp.model.payload.request.PostRequest;
import org.oakkar.chatapp.model.record.PostRecord;
import org.oakkar.chatapp.repository.PostRepository;
import org.oakkar.chatapp.repository.UserRepository;
import org.oakkar.chatapp.service.PostService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final Firestore firestore;

    private final UserRepository userRepository;

    @Override
    public PostRecord createPost(long userId, PostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = Post.builder()
                .description(request.getDescription())
                .user(user)
                .build();
        Post savedPost = postRepository.save(post);
        PostDto postDto = postMapper.mapToDto(savedPost);
        DocumentReference postRef =  firestore.collection("posts").document(savedPost.getId().toString());
        postRef.set(postDto);
        return postMapper.mapToRecord(savedPost);
    }

    @Override
    public void deletePostFromFirestore(String id) {
        firestore.collection("posts").document(id).delete();
    }

    @Override
    public List<PostRecord> getPostsByOwner(String userId) {
        DocumentReference userRef = firestore.collection("users").document(userId);
        CollectionReference postCollectionRef = firestore.collection("posts");
        ApiFuture<QuerySnapshot> resultFuture = postCollectionRef
                .whereEqualTo("owner", userRef).orderBy("updatedDate", Query.Direction.DESCENDING).get();
        List<PostRecord> postRecords = new ArrayList<>();
        try {
            QuerySnapshot result = resultFuture.get();
            log.error(result.getDocuments().toString());
            for (QueryDocumentSnapshot snapshot : result) {

                PostRecord postRecord = new PostRecord(
                        snapshot.getLong("id"),
                        snapshot.getString("description"),
                        Objects.requireNonNull(snapshot.getCreateTime()).toDate().toInstant(),
                        Objects.requireNonNull(snapshot.getUpdateTime()).toDate().toInstant(),
                        null
                );
                postRecords.add(postRecord);
            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return postRecords;
    }

    @Override
    public PostRecord updatePost(String id, PostRequest request) {
        DocumentReference postDocRef = firestore.collection("posts").document(id);

        PostRecord postRecord;
        try {
            PostDto postDto = postDocRef.get().get().toObject(PostDto.class);
            if (postDto != null) {
                postDto.setDescription(request.getDescription());
                postDto.setUpdatedDate(Timestamp.now());
                postDocRef.set(postDto, SetOptions.mergeFields("description", "updatedDate"));
                postRecord = postMapper.maptoRecord(postDto);
            } else {
                postRecord = null;
            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return postRecord;
    }
}
