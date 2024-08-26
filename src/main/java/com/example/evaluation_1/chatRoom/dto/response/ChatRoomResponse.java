package com.example.evaluation_1.chatRoom.dto.response;

import lombok.*;
/**
 * DTO for sending chat room details in the response.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {
    private Long chatRoomId;
    private String user1Email;
    private String user2Email;


}
