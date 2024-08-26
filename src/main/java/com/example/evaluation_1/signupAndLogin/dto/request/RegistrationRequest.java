package com.example.evaluation_1.signupAndLogin.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
/**
 * Data Transfer Object (DTO) for user registration requests.
 */
@Data
@Builder
public class RegistrationRequest {
    private String firstName;

    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    private String phoneNumber;

    private String dob;
    private MultipartFile profileImage;
}
