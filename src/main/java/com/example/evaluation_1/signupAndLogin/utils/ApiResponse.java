package com.example.evaluation_1.signupAndLogin.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {

    private String message;
    private int statusCode;
    private Object data;
    private String token;


}
