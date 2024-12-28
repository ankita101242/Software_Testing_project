package com.example.ProsePetal.Controllers;

import com.example.ProsePetal.Exceptions.ResourceNotFoundException;
import com.example.ProsePetal.Payloads.PostDTO;
import com.example.ProsePetal.Repositories.UserRepo;
import com.example.ProsePetal.Services.JwtService;
import com.example.ProsePetal.Services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtService jwtService;

    @PostMapping(path = "/user/create")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO,
                                        HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid or missing Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix

        try {
            String userEmail = jwtService.extractUsername(jwtToken);
            Integer extractedUserId = userRepo.findUserIdByEmail(userEmail);

            if (extractedUserId == null) {
                throw new ResourceNotFoundException("User", "email", extractedUserId);
            }
            PostDTO createdPost = postService.createPost(postDTO, extractedUserId);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);

        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("User not found for email: " + e.getFieldValue(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating post: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/update/{postId}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable("postId") Integer postId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
        try {
            String userEmail = jwtService.extractUsername(jwtToken);
            Integer extractedUserId = userRepo.findUserIdByEmail(userEmail);
            if (extractedUserId == null) {
                throw new ResourceNotFoundException("User", "email", extractedUserId);
            }
            PostDTO updatedPost = postService.updatePost(postDTO, postId);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/user/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Integer postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<PostDTO>> getAllPostsByUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Integer userId;
        String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
        try {
            String userEmail = jwtService.extractUsername(jwtToken);
            Integer extractedUserId = userRepo.findUserIdByEmail(userEmail);
            userId = extractedUserId;
            if (extractedUserId == null) {
                throw new ResourceNotFoundException("User", "email", extractedUserId);
            }
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<PostDTO> posts = postService.getPostsByUser(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/user/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("postId") Integer postId, HttpServletRequest request) {
        // Extract JWT token from the Authorization header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
        try {
            String userEmail = jwtService.extractUsername(jwtToken);
            Integer extractedUserId = userRepo.findUserIdByEmail(userEmail);

            if (extractedUserId == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            PostDTO post = postService.getPostById(postId).orElse(null);
            if (post != null && post.getUser().getUserId().equals(extractedUserId)) {
                return new ResponseEntity<>(post, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

}