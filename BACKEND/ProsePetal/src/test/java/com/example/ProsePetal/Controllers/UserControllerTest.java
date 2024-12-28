package com.example.ProsePetal.Controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.ProsePetal.Entity.User;
import com.example.ProsePetal.Payloads.UserDTO;
import com.example.ProsePetal.Repositories.UserRepo;
import com.example.ProsePetal.Services.JwtService;
import com.example.ProsePetal.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProfileDetails_ValidAuthorizationHeader() throws Exception {
        // Mock data
        String userEmail = "sejal@prosepetals.com";
        User user = new User();
        user.setEmail(userEmail);
        user.setUserId(3);
        Integer userId = user.getUserId();
        String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWphbEBwcm9zZXBldGFscy5jb20iLCJpYXQiOjE3MTYwMjA0ODIsImV4cCI6MTcxNjYyNTI4Mn0.uZed4d4wHx2iQDFzehOJsWh4uVoZT7-ibY9NJxRZ4k-vIUZEW7OEs42EqbW0c7HKUaSVwLMdp6wl1dgaDJ4B3A";

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Test User");
        userDTO.setEmail(userEmail);
        userDTO.setAbout("Profile Details");

        // Mock behavior
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtService.extractUsername(jwtToken)).thenReturn(userEmail);
        when(userRepo.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userRepo.findUserIdByEmail(userEmail)).thenReturn(userId);
        when(userService.getProfileDetails(userId)).thenReturn(userDTO);

        // Execute the method
        ResponseEntity<UserDTO> response = userController.profileDetails(request);

        // Assert response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    public void testProfileDetails_MissingAuthorizationHeader() throws Exception {
        // Mock behavior
        when(request.getHeader("Authorization")).thenReturn(null);

        // Execute the method
        ResponseEntity<UserDTO> response = userController.profileDetails(request);

        // Assert unauthorized response
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testProfileDetails_InvalidAuthorizationHeaderFormat() throws Exception {
        // Mock behavior
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // Execute the method
        ResponseEntity<UserDTO> response = userController.profileDetails(request);

        // Assert unauthorized response
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteUser_MissingAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);
        ResponseEntity<Void> response = userController.deleteUser(request);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteUser_InvalidAuthorizationHeaderFormat() throws Exception {
        // Mock behavior
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // Execute the method
        ResponseEntity<Void> response = userController.deleteUser(request);

        // Assert unauthorized response
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteUser_SuccessfulDeletion() throws Exception {
        // Mock data
        String userEmail = "test@example.com";
        Integer userId = 1;

        // Mock JWT token generation
        User mockUser = new User();
        mockUser.setEmail(userEmail);
        mockUser.setUserId(userId);
        String jwtToken = jwtService.generateToken(mockUser);
        // Mock behavior
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtService.extractUsername(jwtToken)).thenReturn(userEmail);
        when(userRepo.findUserIdByEmail(userEmail)).thenReturn(userId);
        doNothing().when(userService).deleteUser(userId);

    }

    @Test
    public void testUpdateUser_MissingAuthorizationHeader() throws Exception {
        // Mock data (replace with a sample UserDTO object if needed)
        UserDTO userDTO = new UserDTO(); // Modify based on your UserDTO structure

        // Mock behavior
        when(request.getHeader("Authorization")).thenReturn(null);

        // Execute the method
        ResponseEntity<UserDTO> response = userController.updateUser(request, userDTO);

        // Assert unauthorized response
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    public void testUpdateUser_SuccessfulUpdate() throws Exception {
        // Mock data
        String userEmail = "sejal@prosepetals.com";
        Integer userId = 1;
        String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWphbEBwcm9zZXBldGFscy5jb20iLCJpYXQiOjE3MTYwNTE2MzYsImV4cCI6MTcxNjY1NjQzNn0.GzWkHBQXHqim_jKWPdkS-G2T-ar1aD-QcMUDyxHAEdUFkSSxQ77Es8SsoAAgSqbgcQOQ6BhN6Ph4c2D1PAYIww";
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Sejal Tapadiya");
        userDTO.setAbout("A creative writer");

        // Mock JWT token generation (replace with your actual generation logic)
        // Mock behavior
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtService.extractUsername(jwtToken)).thenReturn(userEmail);
        when(userRepo.findUserIdByEmail(userEmail)).thenReturn(userId);

        UserDTO updatedUser = new UserDTO();
        updatedUser.setName("Sejal Tapadiya");
        updatedUser.setAbout("A creative writer");
        when(userService.updateUser(userId, userDTO)).thenReturn(updatedUser);

        // Execute the method
        ResponseEntity<UserDTO> response = userController.updateUser(request, userDTO);

        // Assert successful update
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedUser.getName(), response.getBody().getName());
    }
    @Test
    public void testUpdateUser_InvalidAuthorizationHeaderFormat() throws Exception {
        // Mock data (replace with a sample UserDTO object if needed)
        UserDTO userDTO = new UserDTO(); // Modify based on your UserDTO structure

        // Mock behavior
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // Execute the method
        ResponseEntity<UserDTO> response = userController.updateUser(request, userDTO);

        // Assert unauthorized response
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

}
