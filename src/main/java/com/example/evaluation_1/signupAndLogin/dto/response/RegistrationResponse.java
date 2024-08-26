package com.example.evaluation_1.signupAndLogin.dto.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
/**
 * Data Transfer Object (DTO) for the response after a successful registration.
 */

@Data
@Builder
public class RegistrationResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dob;
    private String profilePictureUrl;

}
