package com.example.evaluation_1.message.Service;

import com.example.evaluation_1.chatRoom.entity.ChatRoom;
import com.example.evaluation_1.chatRoom.repository.ChatRoomRepository;
import com.example.evaluation_1.message.dto.Mapper.MessageMapper;
import com.example.evaluation_1.message.dto.request.SendMessageRequest;
import com.example.evaluation_1.message.dto.response.MessageData;
import com.example.evaluation_1.message.dto.response.SendMessageResponse;
import com.example.evaluation_1.message.entity.Message;
import com.example.evaluation_1.message.repository.MessageRepository;
import com.example.evaluation_1.signupAndLogin.configuration.JwtService;
import com.example.evaluation_1.signupAndLogin.entity.User;
import com.example.evaluation_1.signupAndLogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService implements IMessageService {

    // Directory where uploaded files will be stored
    private static final String UPLOAD_DIRECTORY = "uploads/";
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request, String token) throws IOException {
        try {
            // Extract sender email from JWT token
            String senderEmail = jwtService.extractUsername(token);
            User sender = userRepository.findByEmail(senderEmail)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));

            User receiver = userRepository.findById(request.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));

            // Check for existing chat room
            ChatRoom chatRoom = chatRoomRepository.findByUser1AndUser2(sender, receiver);
            if (chatRoom == null) {
                // If no chat room exists, create a new one
                chatRoom = ChatRoom.builder()
                        .user1(sender)
                        .user2(receiver)
                        .build();
                chatRoomRepository.save(chatRoom);
            }

            // Handle file upload
            String fileUrl = null;
            if (request.getFile() != null && !request.getFile().isEmpty()) {
                fileUrl = saveFile(request.getFile());
            }

            // Create and save the message
            Message message = MessageMapper.toMessageEntity(request, chatRoom, sender, receiver);
            message.setFileUrl(fileUrl);
            Message savedMessage = messageRepository.save(message);

            // Map saved message to response format
            MessageData messageData = MessageMapper.toMessageData(savedMessage);

            return SendMessageResponse.builder()
                    .roomId(chatRoom.getId())
                    .messageData(messageData)
                    .build();

        } catch (RuntimeException | IOException e) {
            // Log the error or handle it as needed
            throw new RuntimeException("Error occurred while sending the message: " + e.getMessage(), e);
        }
    }

    // Saves an uploaded file to the server.
    private String saveFile(MultipartFile file) throws IOException {
        try {
            if (!Files.exists(Paths.get(UPLOAD_DIRECTORY))) {
                Files.createDirectories(Paths.get(UPLOAD_DIRECTORY));
            }

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIRECTORY + filename);
            Files.write(filePath, file.getBytes());

            return filePath.toString();
        } catch (IOException e) {
            throw new IOException("Error saving file: " + e.getMessage(), e);
        }
    }

    // retrieves messages from a specific chat room sent by a specific sender.
    @Override
    public List<MessageData> getMessagesByRoomAndSender(Long roomId, Long senderId) {
        try {
            // Find the chat room by ID
            ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));
            // Find the sender by ID
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));

            if (chatRoom.getUser1().getId() != senderId && chatRoom.getUser2().getId() != senderId) {
                throw new RuntimeException("Sender with ID " + senderId + " is not a participant in chat room with ID " + roomId);
            }
            // Retrieve and map the messages
            List<Message> messages = messageRepository.findByChatRoomAndSender(chatRoom, sender);
            if (messages.isEmpty()) {
                throw new RuntimeException("No messages found from sender with ID " + senderId + " in chat room " + roomId);
            }

            return messages.stream()
                    .map(MessageMapper::toMessageData)
                    .collect(Collectors.toList());

        } catch (RuntimeException e) {
            throw new RuntimeException("Error occurred while retrieving messages: " + e.getMessage(), e);
        }
    }
//Retrieves all messages from a specific chat room.
    @Override
    public List<MessageData> getAllMessagesByRoomId(Long roomId) {
        try {
            ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));
            // Retrieve and map all messages in the chat room
            List<Message> messages = messageRepository.findByChatRoom(chatRoom);

            if (messages.isEmpty()) {
                throw new RuntimeException("No messages found in chat room with ID " + roomId);
            }

            return messages.stream()
                    .map(MessageMapper::toMessageData)
                    .collect(Collectors.toList());

        } catch (RuntimeException e) {
            throw new RuntimeException("Error occurred while retrieving all messages: " + e.getMessage(), e);
        }
    }
//Deletes a specific message from a chat room.
    @Override
    public void deleteMessage(Long roomId, Long messageId, String token) {
        try {
            // Extract the sender from the token
            String senderEmail = jwtService.extractUsername(token);
            User sender = userRepository.findByEmail(senderEmail)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));

            ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));

            // Find the message by ID and ensure it belongs to the chat room
            Message message = messageRepository.findByIdAndChatRoom(messageId, chatRoom)
                    .orElseThrow(() -> new RuntimeException("Message not found in this chat room"));

            // Delete the message
            messageRepository.delete(message);

        } catch (RuntimeException e) {
            throw new RuntimeException("Error occurred while deleting the message: " + e.getMessage(), e);
        }
    }
   // Updates the text of a specific message in a chat room.
    @Override
    public SendMessageResponse updateMessage(Long roomId, Long messageId, String messageText, String token) {
        try {
            // Extract sender email from token and find the user
            String senderEmail = jwtService.extractUsername(token);
            User sender = userRepository.findByEmail(senderEmail)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));

            ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));

            Message message = messageRepository.findByIdAndChatRoom(messageId, chatRoom)
                    .orElseThrow(() -> new RuntimeException("Message not found in this chat room"));

            // Ensure the user is either the sender or receiver of the message
            if (!message.getSender().equals(sender) && !message.getReceiver().equals(sender)) {
                throw new RuntimeException("User is not authorized to update this message");
            }

            // Update the message text
            message.setMessageText(messageText);
            Message updatedMessage = messageRepository.save(message);

            // Map the updated message to response format
            MessageData messageData = MessageMapper.toMessageData(updatedMessage);

            return SendMessageResponse.builder()
                    .roomId(chatRoom.getId())
                    .messageData(messageData)
                    .build();

        } catch (RuntimeException e) {
            throw new RuntimeException("Error occurred while updating the message: " + e.getMessage(), e);
        }
    }
}
