package com.example.evaluation_1.message.Controller;


import com.example.evaluation_1.message.Service.MessageService;
import com.example.evaluation_1.message.dto.request.SendMessageRequest;
import com.example.evaluation_1.message.dto.response.MessageData;
import com.example.evaluation_1.message.dto.response.SendMessageResponse;
import com.example.evaluation_1.signupAndLogin.utils.ApiResponse;
import com.example.evaluation_1.signupAndLogin.utils.ResponseSender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Object> sendMessage(
            @Valid @ModelAttribute SendMessageRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest httpServletRequest) {

        // Extract token from header
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
        }

        request.setFile(file); // Set the file in the request

        try {
            SendMessageResponse response = messageService.sendMessage(request, token);
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Message sent successfully")
                    .data(response)
                    .statusCode(HttpStatus.OK.value())
                    .token(token)
                    .build();
            return ResponseSender.send(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .message(e.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return ResponseSender.send(apiResponse);
        }
    }

    @GetMapping("/receive")
    public ResponseEntity<Object> receiveMessages(
            @RequestParam Long roomId,
            @RequestParam Long senderId,
            HttpServletRequest httpServletRequest) {

        // Extract token from header
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
        }

        try {
            List<MessageData> messageDataList = messageService.getMessagesByRoomAndSender(roomId, senderId);
            ApiResponse apiResponse = ApiResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Messages retrieved successfully")
                    .data(Map.of("roomId", roomId, "message_data", messageDataList))
                    .token(token)
                    .build();
            return ResponseSender.send(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .message(e.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return ResponseSender.send(apiResponse);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAllMessages(
            @RequestParam("roomId") Long roomId,
            HttpServletRequest httpServletRequest) {

        // Extract token from header
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
        }

        try {
            List<MessageData> messages = messageService.getAllMessagesByRoomId(roomId);
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Messages retrieved successfully")
                    .data(messages)
                    .statusCode(HttpStatus.OK.value())
                    .token(token)
                    .build();
            return ResponseSender.send(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .message(e.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return ResponseSender.send(apiResponse);
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteMessage(
            @RequestParam("roomId") Long roomId,
            @RequestParam("messageId") Long messageId,
            HttpServletRequest httpServletRequest) {

        // Extract token from header
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
        }

        try {
            messageService.deleteMessage(roomId, messageId, token);
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Message deleted successfully")
                    .statusCode(HttpStatus.OK.value())
                    .build();
            return ResponseSender.send(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .message(e.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return ResponseSender.send(apiResponse);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateMessage(
            @RequestParam Long roomId,
            @RequestParam Long messageId,
            @RequestParam String messageText,
            HttpServletRequest httpServletRequest) {

        // Extract token from header
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
        }

        try {
            SendMessageResponse response = messageService.updateMessage(roomId, messageId, messageText, token);
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Message updated successfully")
                    .data(response)
                    .statusCode(HttpStatus.OK.value())
                    .token(token)
                    .build();
            return ResponseSender.send(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .message(e.getMessage())
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .build();
            return ResponseSender.send(apiResponse);
        }
    }
}
