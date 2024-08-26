package com.example.evaluation_1.signupAndLogin.utils;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseSender {
    public static ResponseEntity<Object> send(ApiResponse response) {
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }
}
