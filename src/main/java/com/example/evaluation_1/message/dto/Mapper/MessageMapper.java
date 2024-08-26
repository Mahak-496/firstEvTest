package com.example.evaluation_1.message.dto.Mapper;


import com.example.evaluation_1.chatRoom.entity.ChatRoom;
import com.example.evaluation_1.message.dto.request.SendMessageRequest;
import com.example.evaluation_1.message.dto.response.MessageData;
import com.example.evaluation_1.message.entity.Message;
import com.example.evaluation_1.signupAndLogin.entity.User;

public class MessageMapper {

    // Converts SendMessageRequest to Message entity
    public static Message toMessageEntity(SendMessageRequest request, ChatRoom chatRoom, User sender, User receiver) {
        return Message.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .receiver(receiver)
                .messageText(request.getMessageText())
                .build();
    }

    public static MessageData toMessageData(Message message) {
        return MessageData.builder()
                .messageId(message.getId())
                .senderId(message.getSender().getId())
                .receiverId(message.getReceiver().getId())
                .messageText(message.getMessageText())
                .fileUrl(message.getFileUrl())
                .build();
    }
}
