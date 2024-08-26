package com.example.evaluation_1.chatRoom.dto.mapper;


import com.example.evaluation_1.chatRoom.dto.request.ChatRoomRequest;
import com.example.evaluation_1.chatRoom.dto.response.ChatRoomListResponse;
import com.example.evaluation_1.chatRoom.dto.response.ChatRoomResponse;
import com.example.evaluation_1.chatRoom.entity.ChatRoom;
import com.example.evaluation_1.signupAndLogin.entity.User;
/**
 * Mapper class to convert between ChatRoom entities and various DTOs.
 */
public class ChatRoomMapper {

//Converts a ChatRoomRequest DTO and User entities into a ChatRoom entity.
    public static ChatRoom toChatRoomEntity(ChatRoomRequest request, User user1, User user2) {
        return ChatRoom.builder()
                .user1(user1)
                .user2(user2)
                .build();
    }

    public static ChatRoomListResponse toChatRoomDTO(ChatRoom chatRoom, String currentUserEmail) {

        // Determine the receiver's email, name, and profile image based on the current user's email.
        String receiverEmail = chatRoom.getUser1().getEmail().equals(currentUserEmail) ? chatRoom.getUser2().getEmail() : chatRoom.getUser1().getEmail();
        String receiverName = chatRoom.getUser1().getEmail().equals(currentUserEmail) ? chatRoom.getUser2().getFirstName() + " " + chatRoom.getUser2().getLastName() : chatRoom.getUser1().getFirstName() + " " + chatRoom.getUser1().getLastName();
        String profileImage = chatRoom.getUser1().getEmail().equals(currentUserEmail) ? chatRoom.getUser2().getProfilePictureUrl() : chatRoom.getUser1().getProfilePictureUrl();

        return ChatRoomListResponse.builder()
                .receiverName(receiverName)
                .profileImage(profileImage)
                .build();
    }
//   Converts a ChatRoom entity into a ChatRoomResponse DTO for creating a new chat room.
    public static ChatRoomResponse toCreateChatRoomResponse(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .user1Email(chatRoom.getUser1().getEmail())
                .user2Email(chatRoom.getUser2().getEmail())
                .build();
    }
}
