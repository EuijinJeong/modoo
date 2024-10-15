package com.example.modoo.repository;

import com.example.modoo.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    /**
     *
     * @param senderId: 현재 로그인한 사용자 (구매자, 메세지를 보내는 사람)
     * @return: 이사람이 sender인 ChatRoom을 리스트 형태로 반환함.
     */
    List<ChatRoom> findBySenderId(Long senderId);

    /**
     *
     * @param senderId
     * @param receiverId
     * @return
     */
    List<ChatRoom> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}

