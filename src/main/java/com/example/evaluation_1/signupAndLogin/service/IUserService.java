package com.example.evaluation_1.signupAndLogin.service;


import com.example.evaluation_1.signupAndLogin.dto.request.LoginRequest;
import com.example.evaluation_1.signupAndLogin.dto.request.RegistrationRequest;
import com.example.evaluation_1.signupAndLogin.dto.response.LoginResponse;
import com.example.evaluation_1.signupAndLogin.dto.response.RegistrationResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
//interface for UserService
public interface IUserService {

    RegistrationResponse registerUser(RegistrationRequest registrationRequest, MultipartFile profileImage);

    LoginResponse loginUser(LoginRequest loginRequestDto) throws AuthenticationException;

    String generateToken(String email);
}
