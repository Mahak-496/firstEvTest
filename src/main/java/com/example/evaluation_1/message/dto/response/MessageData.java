package com.example.evaluation_1.message.dto.response;

import lombok.Builder;
import lombok.Data;

//Message Data Dto
@Data
@Builder
public class MessageData {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String messageText;
    private String fileUrl;
}
