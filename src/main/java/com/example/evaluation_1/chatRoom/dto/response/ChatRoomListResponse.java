package com.example.evaluation_1.chatRoom.dto.response;

import lombok.*;
/**
 * DTO for providing a summary of a user's chat rooms.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatRoomListResponse {
    private String receiverName;
    private String profileImage;
}
