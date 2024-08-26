package com.example.evaluation_1.chatRoom.service;


import com.example.evaluation_1.chatRoom.dto.request.ChatRoomRequest;
import com.example.evaluation_1.chatRoom.dto.response.ChatRoomListResponse;
import com.example.evaluation_1.chatRoom.dto.response.ChatRoomResponse;

import java.util.List;

public interface IChatRoomService {
    ChatRoomResponse createChatRoom(ChatRoomRequest request, String user1Email);

    List<ChatRoomListResponse> getAllChatRooms(String userEmail);

    String getUserEmailFromToken(String token);


}
