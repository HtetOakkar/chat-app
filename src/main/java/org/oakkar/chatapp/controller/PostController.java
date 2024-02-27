package org.oakkar.chatapp.controller;

import lombok.RequiredArgsConstructor;
import org.oakkar.chatapp.model.payload.request.PostRequest;
import org.oakkar.chatapp.model.payload.response.ApiResponse;
import org.oakkar.chatapp.model.record.PostRecord;
import org.oakkar.chatapp.security.principal.CurrentUser;
import org.oakkar.chatapp.security.principal.UserPrincipal;
import org.oakkar.chatapp.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<?> createPost(@CurrentUser UserPrincipal userPrincipal, @RequestBody PostRequest request) {
        PostRecord postRecord = postService.createPost(userPrincipal.getId(), request);
        return ApiResponse.success(postRecord, "Post created successfully.");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<?> deletePostFromFirestore(@PathVariable String id) {
        postService.deletePostFromFirestore(id);
        return ApiResponse.success("Post deleted successfully.");
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<?> getPostByOwnerFromFireStore(@CurrentUser UserPrincipal userPrincipal) {
        List<PostRecord> postRecords = postService.getPostsByOwner(userPrincipal.getId().toString());
        return ApiResponse.success(postRecords, "Post fetch success.");
    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<?> updatePost(@PathVariable String id, @RequestBody PostRequest request) {
        PostRecord postRecord = postService.updatePost(id, request);
        return ApiResponse.success(postRecord, "Post update success.");
    }
}
