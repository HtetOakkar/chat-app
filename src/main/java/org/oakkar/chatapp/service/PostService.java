package org.oakkar.chatapp.service;

import org.oakkar.chatapp.model.payload.request.PostRequest;
import org.oakkar.chatapp.model.record.PostRecord;

import java.util.List;

public interface PostService {
    PostRecord createPost(long userId, PostRequest request);

    void deletePostFromFirestore(String id);

    List<PostRecord> getPostsByOwner(String userId);

    PostRecord updatePost(String id, PostRequest request);
}
