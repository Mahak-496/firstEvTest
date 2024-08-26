package com.example.evaluation_1.message.entity;

import com.example.evaluation_1.chatRoom.entity.ChatRoom;
import com.example.evaluation_1.signupAndLogin.entity.User;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

//Message Entity
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Column(name = "file_url")
    private String fileUrl;

}
