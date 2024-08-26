package com.example.evaluation_1.chatRoom.entity;

import com.example.evaluation_1.signupAndLogin.entity.User;
import jakarta.persistence.*;
import lombok.*;
/**
 * Entity representing a chat room between two users.
 */
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ChatRooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

}
