package com.example.evaluation_1.chatRoom.repository;

import com.example.evaluation_1.chatRoom.entity.ChatRoom;
import com.example.evaluation_1.signupAndLogin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Extends JpaRepository to provide basic CRUD operations.
 */
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // Finds all chat rooms
    List<ChatRoom> findByUser1OrUser2(User user1, User user2);

    // Finds a specific chat room involving the two specified users.
    ChatRoom findByUser1AndUser2(User user1, User user2);

}
