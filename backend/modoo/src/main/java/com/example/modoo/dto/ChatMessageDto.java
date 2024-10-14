package com.example.modoo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDto {
    private Long id;
    private Long senderId;
    private Long chatRoomId;
    private String messageContent;
    private LocalDateTime timestamp;
}
