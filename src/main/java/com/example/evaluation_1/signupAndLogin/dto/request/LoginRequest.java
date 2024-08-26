package com.example.evaluation_1.signupAndLogin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
/**
 * Data Transfer Object (DTO) for user login requests.
 */
@Data
@Builder
public class LoginRequest {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;
}
