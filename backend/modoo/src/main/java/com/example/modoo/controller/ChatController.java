package com.example.modoo.controller;

import com.example.modoo.dto.ChatMessageDto;
import com.example.modoo.dto.ChatRoomDto;
import com.example.modoo.entity.Member;
import com.example.modoo.repository.MemberRepository;
import com.example.modoo.service.ChatService;
import com.example.modoo.service.UserEmailLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    UserEmailLookupService userEmailLookupService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 판매자의 상점 아이디를 기반으로 채팅방을 생성하거나 찾는 메서드
     * 모두톡 버튼 클릭하면 실행
     *
     * @param storeId: 판매자의 상점 아이디
     * @return 생성되거나 찾은 채팅방의 정보
     */
    @PostMapping("/go-to-chatroom/{storeId}")
    public ResponseEntity<ChatRoomDto> gotoChatRoom(@PathVariable Long storeId) {

        // 사용자의 이메일 정보를 가져옴
        String userEmail = userEmailLookupService.getCurrentUserEmail();

        // 사용자의 이메일정보를 서비스계층에 전달 ( 반환타입이 Dto임.)
        ChatRoomDto chatRoomDto = chatService.findOrCreateChatRoom(userEmail, storeId);

        // 채팅방 정보를 클라이언트에게 반환
        return ResponseEntity.ok(chatRoomDto);
    }

    /**
     * 현재 로그인한 사용자가 참여하고 있는 모든 채팅방 목록을 가져온느 메서드
     * @return 사용자가 참여하고 있는 채팅방 목록
     */
    @GetMapping("/chat-room-lists")
    public ResponseEntity<List<ChatRoomDto>> getUserChatRoomLists() {
        String userEmail = userEmailLookupService.getCurrentUserEmail(); // 현재 로그인한 사용자의 정보 가져옴
        List<ChatRoomDto> chatRooms = chatService.getUserChatRooms(userEmail);
        return ResponseEntity.ok(chatRooms);
    }

//    @GetMapping("/receivers/{receiverId}")
//    public ResponseEntity<>

    /**
     * 특정 채팅방의 세부 정보를 가져오는 메서드
     * 채팅방 목록에 특정한 채팅 리스트 아이템 클릭할 때 호출됨
     * @param roomId: 채팅방 ID
     * @return 채팅방의 세부 정보와 메세지 목록
     */
    @GetMapping("/chat-rooms/{roomId}")
    public ResponseEntity<Map<String, Object>> getChatRoomDetails(@PathVariable Long roomId) {
        ChatRoomDto chatRoomDto = chatService.getChatRoomById(roomId);
        List<ChatMessageDto> messageDto = chatService.getMessageByChatRoomId(roomId);

        Map<String, Object> response = new HashMap<>();
        response.put("chatRoom", chatRoomDto);
        response.put("messages", messageDto);

        return ResponseEntity.ok(response);
    }

    /**
     * 클라이언트가 메시지를 전송할 때 호출되는 메소드
     * 메세지 입력창에 값을 입력하고 전송하기 버튼을 누르면 호출되는 메소드
     *
     * @param roomId: 채팅방 ID
     * @param chatMessageDto: 클라이언트에서 전송된 메시지 정보
     * @return 생성된 메시지 정보
     */

    @PostMapping("/chat-rooms/{roomId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable Long roomId, @RequestBody ChatMessageDto chatMessageDto) {
        try {
            // chatMessageDto의 chatRoomId를 roomId로 설정
            chatMessageDto.setChatRoomId(roomId);

            // 현재 로그인한 사용자의 정보 (이메일)을 가져옴.
            String userEmail = userEmailLookupService.getCurrentUserEmail();

            // 이메일을 통해 사용자 조회
            Optional<Member> memberOptional = memberRepository.findByEmail(userEmail);
            if(memberOptional.isPresent()) {
                Member member = memberOptional.get();
                chatMessageDto.setSenderId(member.getId());

                // 이후 로직에서 chatMessageDto를 사용하여 메시지를 처리
                chatService.saveMessage(chatMessageDto);

                // 메시지 브로드캐스트 (웹소켓 사용)
                messagingTemplate.convertAndSend(
                        "/topic/chatroom/" + chatMessageDto.getChatRoomId(),
                        chatMessageDto
                );
                return ResponseEntity.ok().build();
            } else {
                // 예외처리: 사용자를 찾을 수 없는 경우
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }
}
