package com.example.modoo.dto;

import com.example.modoo.entity.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChatRoomDto {
    private Long id;
    private Long sender;
    private Long receiver;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private List<ChatMessageDto> chatMessageList; // 해당 유저가 생성한 chatRoomList 목록
    private Long currentUserId; // 현재 사용자의 ID 추가
}
