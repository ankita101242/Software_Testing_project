package com.example.ProsePetal.Controllers;
import com.example.ProsePetal.Payloads.PostDTO;
import com.example.ProsePetal.Payloads.UserDTO;
import com.example.ProsePetal.Repositories.UserRepo;
import com.example.ProsePetal.Services.JwtService;
import com.example.ProsePetal.Services.PostService;
import com.example.ProsePetal.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@NoArgsConstructor
public class UserController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired

    private UserRepo userRepo;

    @GetMapping("/viewProfile")
    public ResponseEntity<UserDTO> profileDetails(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix

        try {

            String userEmail = jwtService.extractUsername(jwtToken);
            Integer userId = userRepo.findUserIdByEmail(userEmail);
            UserDTO user = userService.getProfileDetails(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
        try {
            String userEmail = jwtService.extractUsername(jwtToken);
            Integer userId = userRepo.findUserIdByEmail(userEmail);
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
        try {
            String userEmail = jwtService.extractUsername(jwtToken);
            Integer userId = userRepo.findUserIdByEmail(userEmail);
            UserDTO updatedUser = userService.updateUser(userId, userDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
