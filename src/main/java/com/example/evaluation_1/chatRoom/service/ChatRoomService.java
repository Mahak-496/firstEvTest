package com.example.evaluation_1.chatRoom.service;

import com.example.evaluation_1.chatRoom.dto.mapper.ChatRoomMapper;
import com.example.evaluation_1.chatRoom.dto.request.ChatRoomRequest;
import com.example.evaluation_1.chatRoom.dto.response.ChatRoomListResponse;
import com.example.evaluation_1.chatRoom.dto.response.ChatRoomResponse;
import com.example.evaluation_1.chatRoom.entity.ChatRoom;
import com.example.evaluation_1.chatRoom.repository.ChatRoomRepository;
import com.example.evaluation_1.signupAndLogin.configuration.JwtService;
import com.example.evaluation_1.signupAndLogin.entity.User;
import com.example.evaluation_1.signupAndLogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing chat rooms.
 */
@Service
public class ChatRoomService implements IChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    //Creates a new chat room or returns an existing one if it already exists.
    @Override
    public ChatRoomResponse createChatRoom(ChatRoomRequest request, String user1Email) {
        try {
            // Fetch the first user by email
            User user1 = userRepository.findByEmail(user1Email)
                    .orElseThrow(() -> new RuntimeException("User with email " + user1Email + " not found"));

            // Fetch the second user by email
            User user2 = userRepository.findByEmail(request.getUser2Email())
                    .orElseThrow(() -> new RuntimeException("User with email " + request.getUser2Email() + " not found"));

            ChatRoom existingChatRoom = chatRoomRepository.findByUser1AndUser2(user1, user2);

            if (existingChatRoom != null) {
                // Use mapper to build the response for an existing chat room
                return ChatRoomMapper.toCreateChatRoomResponse(existingChatRoom);
            }

            // If the chat room does not exist, create a new one
            ChatRoom chatRoom = ChatRoomMapper.toChatRoomEntity(request, user1, user2);
            ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

            // Use mapper to build the response for a newly created chat room
            return ChatRoomMapper.toCreateChatRoomResponse(savedChatRoom);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating the chat room", e);
        }
    }

    //Retrieves all chat rooms for a given user.
    @Override
    public List<ChatRoomListResponse> getAllChatRooms(String userEmail) {
        try {
            // Fetch the user by email
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User with email " + userEmail + " not found"));

            // Retrieve all chat rooms where the user is either user1 or user2
            List<ChatRoom> chatRooms = chatRoomRepository.findByUser1OrUser2(user, user);

            // Map each chat room to a ChatRoomListResponse DTO
            return chatRooms.stream()
                    .map(chatRoom -> ChatRoomMapper.toChatRoomDTO(chatRoom, userEmail))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving chat rooms", e);
        }
    }

    //Extracts the user's email from a given JWT token.
    @Override
    public String getUserEmailFromToken(String token) {
        try {
            return jwtService.extractUsername(token);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while extracting the username from the token", e);
        }
    }
}
