package com.example.modoo.service;

import com.example.modoo.dto.ChatMessageDto;
import com.example.modoo.dto.ChatRoomDto;
import com.example.modoo.dto.MemberResponseDto;
import com.example.modoo.entity.ChatMessage;
import com.example.modoo.entity.ChatRoom;
import com.example.modoo.entity.Member;
import com.example.modoo.entity.Store;
import com.example.modoo.repository.ChatMessageRepository;
import com.example.modoo.repository.ChatRoomRepository;
import com.example.modoo.repository.MemberRepository;
import com.example.modoo.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;


    /**
     * 현재 로그인한 사용자의 이메일과 상점의 아이디 값을 기반으로 존재하는 채팅방을 찾거나,
     * 없다면 이 정보를 기반으로 채팅방을 생성하는 메서드
     *
     * @param userEmail : 현재 로그인한 사용자 이메일( sender )
     * @param storeId : 사용자가 메세지를 보내는 사람의 상점 아이디 값 ( receiver )
     * @return
     */
    public ChatRoomDto findOrCreateChatRoom(String userEmail, long storeId) {

        // 이메일을 통해 사용자 정보 조회
        Member sender = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Member 엔티티에서 해당하는 email을 찾을 수 없습니다: " + userEmail));

        // StoreId를 통해 상점 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 상점의 판매자를 찾을 수 없습니다: " + storeId));

        // 상점 정보로 사용자 조회
        Member receiver = store.getMember();

        // 두 사용자 간의 채팅방 조회
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());

        // 채팅방 객체 생성
        ChatRoom chatRoom;

        if(chatRoomOptional.isPresent()) {
            // 채팅방이 존재하는 경우
            // 기존 채팅방을 가져와서 반환한다.
            chatRoom = chatRoomOptional.get();
        } else {
            // 새로운 채팅방 생성함
            chatRoom = new ChatRoom();
            chatRoom.setSenderId(sender.getId());
            chatRoom.setReceiverId(receiver.getId());
            chatRoom = chatRoomRepository.save(chatRoom);
        }

        // DTO로 변환하여 반환함.
        return convertToDto(chatRoom, sender.getId());
    }

    /**
     * 현재 로그인한 사용자가 함여하고 있는 채팅방의 리스트를 가져오는 메서드
     *
     * @param email : 현재 로그인한 사용자의 이메일 정보
     * @return
     */
    public List<ChatRoomDto> getUserChatRooms(String email) {
        // 현재 로그인한 사용자의 정보
        Member user = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 사용자의 이메일을 찾을 수 없습니다." + email));

        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderId(user.getId());
        return chatRooms.stream()
                .map(chatRoom -> convertToDto(chatRoom, user.getId())) // currentUserId로 user.getId() 전달
                .collect(Collectors.toList());
    }

    /**
     * roomId를 파라미터로 받아서 이에 해당하는 채팅방을 가져오는 역할을 수행하는 메소드
     *
     * @param roomId
     * @return
     */
    public ChatRoomDto getChatRoomById(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found with id: " + roomId));

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(roomId);
        List<ChatMessageDto> messageDtos = messages.stream().map(this::convertToDto).collect(Collectors.toList());

        ChatRoomDto chatRoomDto = convertToDto(chatRoom, chatRoom.getSenderId());
        chatRoomDto.setChatMessageList(messageDtos); // 메시지 목록을 DTO에 설정
        return chatRoomDto;
    }

    /**
     * 채팅방의 roomId를 통해 채팅방 안에 있던 메세지의 내용들을 가져와서 반환하는 메서드
     *
     * @param roomId
     * @return
     */
    public List<ChatMessageDto> getMessageByChatRoomId(Long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(roomId);
        return messages.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * 메시지를 저장하는 메서드
     *
     * @param chatMessageDto
     */
    public void saveMessage(ChatMessageDto chatMessageDto) {
        // ChatRoom 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("해당 채팅방을 찾을 수 없습니다: " + chatMessageDto.getChatRoomId()));

        // Member 조회
        Member sender = memberRepository.findById(chatMessageDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("발신자를 찾을 수 없습니다: " + chatMessageDto.getSenderId()));

        // ChatMessage 엔티티 생성 및 저장
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setMessageContent(chatMessageDto.getMessageContent()); // 수정된 부분


        chatMessage.setTimestamp(chatMessageDto.getTimestamp() != null ?
                chatMessageDto.getTimestamp() : LocalDateTime.now());

        chatMessageRepository.save(chatMessage);

        // FIXME: 이거 마저 코드 작성해야함!
//        // ChatRoom의 last_message와 last_message_time 업데이트
//        chatRoom.setLastMessage(chatMessageDto.getMessageContent());
//        chatRoom.setLastMessageTime(LocalDateTime.now

        chatRoomRepository.save(chatRoom); // 변경 사항을 저장
    }

    /**
     *
     * @param chatRoom 엔티티 클래스
     * @return 엔티티 클래스에 있는 정보를 Dto 객체에 담아 리턴함.
     */
    public ChatRoomDto convertToDto(ChatRoom chatRoom, Long currentUserId) {
        ChatRoomDto dto = new ChatRoomDto();
        dto.setId(chatRoom.getId());
        dto.setReceiver(chatRoom.getReceiverId());
        dto.setSender(chatRoom.getSenderId());
        dto.setLastMessage(chatRoom.getLastMessage());
        dto.setLastMessageTime(chatRoom.getLastMessageTime());
        dto.setCurrentUserId(currentUserId); // 현재 사용자의 ID 설정

        // 메시지 목록을 DTO로 변환
        dto.setChatMessageList(chatRoom.getChatMessages().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public ChatMessageDto convertToDto(ChatMessage message) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setMessageContent(message.getMessageContent());
        dto.setTimestamp(message.getTimestamp());

        return dto;
    }
}
