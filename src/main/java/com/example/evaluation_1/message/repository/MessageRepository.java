package com.example.evaluation_1.message.repository;

import com.example.evaluation_1.chatRoom.entity.ChatRoom;
import com.example.evaluation_1.message.entity.Message;
import com.example.evaluation_1.signupAndLogin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    //Finds all messages in a specific chat room sent by a particular sender.
    List<Message> findByChatRoomAndSender(ChatRoom chatRoom, User sender);

    //Finds all messages in a specific chat room.
    List<Message> findByChatRoom(ChatRoom chatRoom);

    //Finds a message by its ID and the associated chat room.
    Optional<Message> findByIdAndChatRoom(Long id, ChatRoom chatRoom);

}
