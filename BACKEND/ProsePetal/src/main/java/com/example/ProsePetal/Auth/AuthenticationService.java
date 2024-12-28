package com.example.ProsePetal.Auth;

import com.example.ProsePetal.Entity.Role;
import com.example.ProsePetal.Entity.User;
import com.example.ProsePetal.Repositories.UserRepo;
import com.example.ProsePetal.Services.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Log registration attempt
        logger.info("Registering new user: {}", request.getEmail());

        // Build your application's User entity
        User user = new User();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Role.USER); // Could be dynamic based on request
        user.setName(request.getName());
        user.setAbout(request.getAbout());

        // Save user to database
        userRepo.save(user);

        // Generate JWT token
        var jwtToken = jwtService.generateToken(user);

        // Log successful registration
        logger.info("User registered successfully: {}", request.getEmail());

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate user credentials
        logger.info("Authenticating user: {}", request.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Retrieve the user from the database
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.error("User not found: {}", request.getEmail());
                    return new UsernameNotFoundException("User not found");
                });

        // Generate JWT token
        var jwtToken = jwtService.generateToken(user);

        // Log successful authentication
        logger.info("User authenticated successfully: {}", request.getEmail());

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        String currentEmail = request.getEmail();
        System.out.println("Email from request: " + currentEmail);

        // Debug the repository call
        Optional<User> userOpt = userRepo.findByEmail(currentEmail);
        if (userOpt.isEmpty()) {
            System.out.println("User not found for email: " + currentEmail);
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        System.out.println("User retrieved: " + user);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
    }
}
