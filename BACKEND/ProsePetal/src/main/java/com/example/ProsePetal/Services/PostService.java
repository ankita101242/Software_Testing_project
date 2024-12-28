package com.example.ProsePetal.Services;

import com.example.ProsePetal.Payloads.PostDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService {
    // Create post
    PostDTO createPost(PostDTO postDTO, Integer userId);

    // Update post
    PostDTO updatePost(PostDTO postDTO, Integer postId);

    // Delete post
    void deletePost(Integer postId);

    // Get all posts (consider using Page for pagination)
    List<PostDTO> getAllPosts();

    // Get post by ID
    Optional<PostDTO> getPostById(Integer postId);

    // Get posts by user
    List<PostDTO> getPostsByUser(Integer userId);

    // Search posts (consider using advanced search features)
    List<PostDTO> searchPosts(String keyword);

}