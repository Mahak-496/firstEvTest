package com.example.evaluation_1.message.Service;

import com.example.evaluation_1.message.dto.request.SendMessageRequest;
import com.example.evaluation_1.message.dto.response.MessageData;
import com.example.evaluation_1.message.dto.response.SendMessageResponse;

import java.io.IOException;
import java.util.List;

public interface IMessageService {
    SendMessageResponse sendMessage(SendMessageRequest request, String token) throws IOException;

    List<MessageData> getMessagesByRoomAndSender(Long roomId, Long senderId);

    List<MessageData> getAllMessagesByRoomId(Long roomId);

    void deleteMessage(Long roomId, Long messageId, String token);

    SendMessageResponse updateMessage(Long roomId, Long messageId, String messageText, String token);
}
