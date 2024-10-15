import React, { useEffect, useState } from "react";
import ChatRoomList from "./ChatRoomList";
import ChatRoom from "./ChatRoom";
import "../css/chat_room_wrapper.css";

/**
 *
 *
 * @param storeId
 * @param roomId
 * @returns {Element}
 * @constructor
 */
const ChatRoomWrapper = ({ roomId }) => {
  const [selectedChat, setSelectedChat] = useState(null);

  useEffect(() => {
    // 필요한 초기화 작업
    if (roomId) {
      setSelectedChat({ id: roomId });
    }
  }, [roomId]);

  const handleChatSelect = (chat) => {
    setSelectedChat(chat);
    console.log("선택된 채팅방의 고유 아이디값: ", chat.id);
  };

  return (
    <div className="chat-room-wrapper">
      <ChatRoomList setSelectedChat={handleChatSelect} />
      {selectedChat && <ChatRoom roomId={selectedChat.id} />}
    </div>
  );
};

export default ChatRoomWrapper;
