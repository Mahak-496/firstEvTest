package com.example.evaluation_1.message.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendMessageResponse {
    private Long roomId;
    private MessageData messageData;
}
