package com.example.modoo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "chat_room")
@Getter
@Setter
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId; // 판매자

    @Column(name = "sender_id", nullable = false)
    private Long senderId; // 구매자

    @Column(name = "last_message")
    private String lastMessage;

    @Column(name = "last_message_time")
    private LocalDateTime lastMessageTime;

    // OneToMany를 사용하려면 List, Set, Map과 같은 컬렉션 타입이어야 한다.
    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessages;
}
