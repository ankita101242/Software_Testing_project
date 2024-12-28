package com.example.ProsePetal.Controllers;

import com.example.ProsePetal.Entity.User;
import com.example.ProsePetal.Payloads.MinUserDTO;
import com.example.ProsePetal.Payloads.PostDTO;
import com.example.ProsePetal.Repositories.UserRepo;
import com.example.ProsePetal.Services.PostService;
import com.example.ProsePetal.Services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostControllerTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PostController postController;
    @Mock
    private UserRepo userRepo;

    @Mock
    private PostService postService;

    @Mock
    private JwtService jwtService;
    private String jwtToken;
    private String userEmail;
    private Integer userId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtToken = "test.jwt.token";
        userEmail = "test@example.com";
        userId = 1;
    }

    @Test
    public void testCreatePost_MissingAuthorizationHeader() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("The Power of Comics");
        postDTO.setContent("For a long time, comic books and graphic novels were geared toward children on the basis that, because they have pictures, they’re not “real books.” As a child, I wasn’t allowed to read comics for that very reason. I read my first comic book as an adult when I met my husband, who is the comic buyer for our local shop. Today, I am the mother of two girls who are obsessed with manga (Japanese comics). This year alone, I have read over 400 comic books. It’s safe to say that comics play a very large part of our family’s reading life.\n" +
                "\n" +
                "Comics versus graphic novels\n" +
                "\n" +
                "Stylistically, comics and graphic novels are very similar. Comic books are usually about 24 pages and are released in single issues usually once or twice a month. These individual books often form an ongoing story that spans several issues. Like TV shows, they are published regularly and collected in what are called trades. Batman, Teenage Mutant Ninja Turtles, and Bone are all popular examples of comics.\n" +
                "\n" +
                "Graphic novels are basically longer versions of comics. Usually, graphic novels tell one full story and can be a couple hundred pages long. Examples include Amulet, The Witch Boy, and Smile.");
        MinUserDTO user = new MinUserDTO();
        user.setUserId(1);
        user.setEmail("sejal@prosepetals.com");
        postDTO.setUser(user);


        // Mocking the request to return null for the Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // Execute the method
        ResponseEntity<?> response = postController.createPost(postDTO, request);

        // Assert unauthorized response
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    public void testCreatePost_SuccessfulCreation() throws Exception {
        // Mock data
        String userEmail = "test@example.com";
        Integer userId = 1;
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("The Power of Comics");
        postDTO.setContent("For a long time, comic books and graphic novels were geared toward children on the basis that, because they have pictures, they’re not “real books.” As a child, I wasn’t allowed to read comics for that very reason. I read my first comic book as an adult when I met my husband, who is the comic buyer for our local shop. Today, I am the mother of two girls who are obsessed with manga (Japanese comics). This year alone, I have read over 400 comic books. It’s safe to say that comics play a very large part of our family’s reading life.\n" +
                "\n" +
                "Comics versus graphic novels\n" +
                "\n" +
                "Stylistically, comics and graphic novels are very similar. Comic books are usually about 24 pages and are released in single issues usually once or twice a month. These individual books often form an ongoing story that spans several issues. Like TV shows, they are published regularly and collected in what are called trades. Batman, Teenage Mutant Ninja Turtles, and Bone are all popular examples of comics.\n" +
                "\n" +
                "Graphic novels are basically longer versions of comics. Usually, graphic novels tell one full story and can be a couple hundred pages long. Examples include Amulet, The Witch Boy, and Smile.");
        MinUserDTO user = new MinUserDTO();
        user.setUserId(1);
        user.setEmail("sejal@prosepetals.com");
        postDTO.setUser(user);


        String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWphbEBwcm9zZXBldGFscy5jb20iLCJpYXQiOjE3MTYwNTE2MzYsImV4cCI6MTcxNjY1NjQzNn0.GzWkHBQXHqim_jKWPdkS-G2T-ar1aD-QcMUDyxHAEdUFkSSxQ77Es8SsoAAgSqbgcQOQ6BhN6Ph4c2D1PAYIww";

        // Mock behavior
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtService.extractUsername(jwtToken)).thenReturn(userEmail);
        when(userRepo.findUserIdByEmail(userEmail)).thenReturn(userId);
        when(postService.createPost(postDTO, userId)).thenReturn(postDTO); // Mock successful post creation

        // Execute the method
        ResponseEntity<?> response = postController.createPost(postDTO, request);

        // Assert successful creation response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

    }

    @Test
    public void testUpdatePost_ValidAuthorization() throws Exception {
        // Prepare post data for update
        PostDTO updatedPostDTO = new PostDTO();
        updatedPostDTO.setPostId(1); // Existing post ID
        updatedPostDTO.setTitle("Updated Post Title");
        updatedPostDTO.setContent("Updated post content");
        MinUserDTO user = new MinUserDTO();
        user.setUserId(1);
        user.setEmail("sejal@prosepetals.com");
        updatedPostDTO.setUser(user);
        Integer postId = updatedPostDTO.getPostId();
        // Mock JWT token and user extraction
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdWRhdGhlQGVtYWlsLmNvbSIsInJvbGUiOiJ1c2VyIiwiaWF0IjoxNjU3MDk3MjAwLCJleHAiOjE2NTc2NTcyMDB9.KSmFNKUuSrLxxxxxx";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtService.extractUsername(jwtToken)).thenReturn("sejal@prosepetals.com");
        when(userRepo.findUserIdByEmail("sejal@prosepetals.com")).thenReturn(1);

        // Mock successful update by postService
        when(postService.updatePost(updatedPostDTO, 1)).thenReturn(updatedPostDTO);

        // Execute the method
        ResponseEntity<PostDTO> response = postController.updatePost(updatedPostDTO, postId, request);

        // Assert successful update response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody()); // Assert the response body is not null
        assertEquals(updatedPostDTO, response.getBody()); // Assert the updated post matches the provided data
    }
    @Test
    public void testUpdatePost_Success() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Updated Title");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtService.extractUsername(jwtToken)).thenReturn(userEmail);
        when(userRepo.findUserIdByEmail(userEmail)).thenReturn(userId);
        when(postService.updatePost(postDTO, 1)).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = postController.updatePost(postDTO, 1, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDTO, response.getBody());
    }
    @Test
    public void testUpdatePost_Unauthorized() {
        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<PostDTO> response = postController.updatePost(new PostDTO(), 1, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeletePost_Success() {
        doNothing().when(postService).deletePost(1);

        ResponseEntity<Void> response = postController.deletePost(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetAllPostsByUser_Success() throws Exception {
        List<PostDTO> postList = new ArrayList<>();
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Test Post");
        postList.add(postDTO);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtService.extractUsername(jwtToken)).thenReturn(userEmail);
        when(userRepo.findUserIdByEmail(userEmail)).thenReturn(userId);
        when(postService.getPostsByUser(userId)).thenReturn(postList);

        ResponseEntity<List<PostDTO>> response = postController.getAllPostsByUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postList, response.getBody());
    }

    @Test
    public void testGetAllPostsByUser_Unauthorized() {
        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<List<PostDTO>> response = postController.getAllPostsByUser(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetPostById_Success() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Test Post");
        MinUserDTO user = new MinUserDTO();
        user.setUserId(userId);
        postDTO.setUser(user);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtService.extractUsername(jwtToken)).thenReturn(userEmail);
        when(userRepo.findUserIdByEmail(userEmail)).thenReturn(userId);
        when(postService.getPostById(1)).thenReturn(Optional.of(postDTO));

        ResponseEntity<PostDTO> response = postController.getPostById(1, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDTO, response.getBody());
    }

    @Test
    public void testGetPostById_Unauthorized() {
        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<PostDTO> response = postController.getPostById(1, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetPostById_NotFound() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtService.extractUsername(jwtToken)).thenReturn(userEmail);
        when(userRepo.findUserIdByEmail(userEmail)).thenReturn(userId);
        when(postService.getPostById(1)).thenReturn(Optional.empty());

        ResponseEntity<PostDTO> response = postController.getPostById(1, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}

