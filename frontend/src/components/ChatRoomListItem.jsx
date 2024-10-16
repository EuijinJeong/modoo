import React, { useEffect, useState } from "react";
import "../css/chat-room-list-item.css";
import axios from "axios";

const ChatRoomListItem = ({ room, onClick }) => {
  const [receiverInfo, setReceiverInfo] = useState(null);

  /**
   *
   */
  useEffect(() => {
    const fetchReceiverInfo = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`/api/receivers/${room.receiverId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        // 서버에서 받은 receiver 정보를 상태에 저장
        setReceiverInfo(response.data);
        console.log(response);
      } catch (e) {
        console.error("Receiver 정보를 가져오는 중 오류가 발생했습니다.", e);
      }
    };

    if (room.receiverId) {
      fetchReceiverInfo();
    }
  }, [room.receiverId]);

  const lastMessage = room.lastMessage || "대화 내용 없음";
  const lastMessageTime = room.lastMessageTime
    ? new Date(room.lastMessageTime).toLocaleDateString()
    : "시간 정보 없음"; // 시간 정보가 없을 경우 대비

  const receiverName =
    receiverInfo?.email || receiverInfo?.studentId || "알 수 없는 사용자";

  return (
    <div className="chat-list-item" onClick={onClick}>
      <div className="chat-user">{receiverName}</div>
      <div className="chat-preview">{lastMessage}</div>
      <div className="chat-time">{lastMessageTime}</div>
    </div>
  );
};

export default ChatRoomListItem;
