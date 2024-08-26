package com.example.evaluation_1.signupAndLogin.dto.mapper;

import com.example.evaluation_1.signupAndLogin.dto.request.LoginRequest;
import com.example.evaluation_1.signupAndLogin.dto.request.RegistrationRequest;
import com.example.evaluation_1.signupAndLogin.dto.response.LoginResponse;
import com.example.evaluation_1.signupAndLogin.dto.response.RegistrationResponse;
import com.example.evaluation_1.signupAndLogin.entity.User;
/**
 * Mapper class to convert between User entities and various DTOs.
 */
public class UserMapper {

    //   Converts a RegistrationRequest DTO to a User entity.
    public static User toUserEntity(RegistrationRequest registrationRequest) {
        return User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(registrationRequest.getPassword())
                .phoneNumber(registrationRequest.getPhoneNumber())
                .dob(registrationRequest.getDob())
                .build();
    }

    //  Converts a User entity to a RegistrationResponse DTO.
    public static RegistrationResponse toRegistrationResponse(User user) {
        return RegistrationResponse.builder()
                .firstName(user.getFirstName().trim())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }

    // Converts a User entity and an authentication token to a LoginResponse DTO.
    public static LoginResponse toLoginResponse(User user, String token) {
        return LoginResponse.builder()
                .firstName(user.getFirstName().trim())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .profilePictureUrl(user.getProfilePictureUrl())
                .build();
    }
}
