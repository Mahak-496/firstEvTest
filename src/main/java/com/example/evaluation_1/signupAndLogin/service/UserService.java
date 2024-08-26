package com.example.evaluation_1.signupAndLogin.service;

import com.example.evaluation_1.signupAndLogin.configuration.JwtService;
import com.example.evaluation_1.signupAndLogin.dto.mapper.UserMapper;
import com.example.evaluation_1.signupAndLogin.dto.request.LoginRequest;
import com.example.evaluation_1.signupAndLogin.dto.request.RegistrationRequest;
import com.example.evaluation_1.signupAndLogin.dto.response.LoginResponse;
import com.example.evaluation_1.signupAndLogin.dto.response.RegistrationResponse;
import com.example.evaluation_1.signupAndLogin.entity.User;
import com.example.evaluation_1.signupAndLogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service class for handling user-related operations including registration, login, and token management.
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Value("${file.storage.location}")
    private String storageLocation;

    //Registers a new user, handles profile image upload, and generates a JWT token.
    @Override
    public RegistrationResponse registerUser(RegistrationRequest registrationRequest, MultipartFile profileImage) {
        // Check if user with the given email already exists
        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = UserMapper.toUserEntity(registrationRequest);

        // Handle profile image upload
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
                Path filePath = Paths.get(storageLocation, fileName);
                Files.createDirectories(filePath.getParent());
                profileImage.transferTo(filePath.toFile());
                String imageUrl = "http://localhost:8080/images/" + fileName;
                user.setProfilePictureUrl(imageUrl);
            }catch (IOException e) {
                throw new RuntimeException("Could not save image", e);
            }
        }

        String token = jwtService.generateToken(registrationRequest.getEmail());
        user.setToken(token);

        // Encode password and generate token
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        return UserMapper.toRegistrationResponse(registeredUser);
    }

    //Authenticates a user and generates a JWT token upon successful login.
    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) throws AuthenticationException {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                // Generate token and update user entity
                String token = jwtService.generateToken(loginRequest.getEmail());
                User user = userRepository.findByEmail(loginRequest.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                user.setToken(token);
                userRepository.save(user);

                // Return login response
                return UserMapper.toLoginResponse(user, token);
            } else {
                throw new RuntimeException("Invalid email or password");
            }
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid email or password");
        }
    }

    //Generates a JWT token for the specified email.
    @Override
    public String generateToken(String email) {
        return jwtService.generateToken(email);
    }
}
