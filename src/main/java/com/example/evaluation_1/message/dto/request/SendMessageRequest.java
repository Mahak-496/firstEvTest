package com.example.evaluation_1.message.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
public class SendMessageRequest {
    private Long receiverId;
    private String messageText;
    private MultipartFile file;
    private Long roomId;
}
