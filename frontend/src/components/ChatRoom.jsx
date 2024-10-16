import React, { useEffect, useState } from "react";
import axios from "axios";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import "../css/chat-room.css";
import ChatMessageInput from "./ChatMessageInput";
import ChatMessageBubble from "./ChatMessageBubble";

const ChatRoom = ({ roomId }) => {
  const [chatRoom, setChatRoom] = useState(null);
  const [messages, setMessages] = useState([]);
  const [stompClient, setStompClient] = useState(null); // WebSocket 클라이언트 상태

  useEffect(() => {
    if (roomId) {
      const fetchChatRoomDetails = async () => {
        try {
          const token = localStorage.getItem("token");
          const response = await axios.get(`/api/chat-rooms/${roomId}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });

          console.log("Chat room response: ", response.data);

          // chatRoom 데이터를 설정하고, messages를 chatMessageList로 설정
          setChatRoom(response.data.chatRoom);
          //   setMessages(response.data.chatRoom.chatMessageList || []); // chatMessageList 사용
          setMessages(response.data.messages || []);
        } catch (error) {
          console.error(
            "ChatRoom을 가져오는 과정에서 서버상 오류가 발생했습니다.",
            error
          );
        }
      };
      fetchChatRoomDetails();

      // WebSocket 연결 설정
      const socket = new SockJS("http://localhost:8080/ws"); // 서버의 WebSocket 엔드포인트
      const stompClient = new Client({
        webSocketFactory: () => socket, // SockJS를 사용한 WebSocket 설정
        debug: (str) => {
          console.log(str);
        },
        reconnectDelay: 5000, // 재연결 시도 시간 설정 (5초)
        onConnect: () => {
          console.log("Connected to WebSocket");
          // 특정 채팅방을 구독
          stompClient.subscribe(`/topic/chatroom/${roomId}`, (message) => {
            const newMessage = JSON.parse(message.body);
            // 새 메시지가 도착하면 상태 업데이트
            setMessages((prevMessages) => [...prevMessages, newMessage]);
          });
        },
      });

      stompClient.activate(); // WebSocket 활성화

      // WebSocket 클라이언트 저장
      setStompClient(stompClient);

      // 컴포넌트 언마운트 시 WebSocket 연결 해제
      return () => {
        if (stompClient) {
          console.log("Disconnecting WebSocket");
          stompClient.deactivate();
        }
      };
    }
  }, [roomId]);

  /**
   * 메세지를 전송할 때 호출되는 메소드
   *
   * @param messageContent : 사용자가 입력한 메세지 내용
   * @returns {Promise<void>}
   */
  const handleSendMessage = async (messageContent) => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        `/api/chat-rooms/${roomId}/messages`,
        { messageContent }, // messageContent만 보냄
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // 성공적으로 메시지를 전송한 후, 로컬 상태를 즉시 업데이트합니다.
      setMessages((prevMessages) => [
        ...prevMessages,
        {
          messageContent: messageContent, // 사용자가 보낸 메시지 내용
          senderId: chatRoom.sender, // 현재 사용자 ID
          timestamp: new Date().toISOString(), // 현재 시간
        },
      ]);
    } catch (error) {
      console.error("메세지 전송 중 오류가 발생했습니다.", error);
    }
  };

  if (!chatRoom) {
    return <div>Loading ...</div>;
  } else if (messages.length === 0) {
    return (
      <div>
        <div>No messages in this chat room.</div>
        <ChatMessageInput onSendMessage={handleSendMessage} />{" "}
        {/* 메시지를 보낼 수 있도록 입력 폼 표시 */}
      </div>
    );
  }

  return (
    <div className="chat-room">
      <div className="messages">
        {messages && messages.length > 0 ? (
          messages.map((message, index) => (
            <ChatMessageBubble
              key={index}
              message={message.messageContent || "No Content"} // 메시지 내용이 없을 경우 대비
              isOwnMessage={message.senderId === chatRoom.sender} // 현재 사용자와 발신자 비교
            />
          ))
        ) : (
          <div>No messages found.</div>
        )}
      </div>
      <ChatMessageInput onSendMessage={handleSendMessage} />
    </div>
  );
};

export default ChatRoom;
