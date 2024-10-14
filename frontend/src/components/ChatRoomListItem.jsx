import React, {useEffect, useState} from 'react';
import "../css/chat-room-list-item.css"
import message from "sockjs-client/lib/transport/lib/buffered-sender";
import axios from "axios";

const ChatRoomListItem = ({ room, onClick }) => {
    const [receiverInfo, setReceiverInfo] = useState(null);

    // TODO: 이거 사용자명 내가 보내는 대상의 이메일 또는 학번 또는 상점 닉네임 가져오는거 로직 작성해야함.
    // receiver 정보를 기반으로 사용자명 표시
    useEffect(() => {
        const fetchReceiverInfo = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await axios.get(`/api/receivers/${room.receiverId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
            } catch (e) {
                console.error("Receiver 정보를 가져오는 중 오류가 발생했습니다.", e);
            }
        };

        if(room.receiverId) {
            fetchReceiverInfo();
        }
    }, [room.receiverId]);

    const lastMessage = room.lastMessage || "대화 내용 없음"; // 대화 내용이 없을 경우 대비
    const lastMessageTime = room.lastMessageTime
        ? new Date(room.lastMessageTime).toLocaleDateString()
        : "시간 정보 없음"; // 시간 정보가 없을 경우 대비

    const receiverName = room.receiverName || room.receiverEmail || "알 수 없는 사용자";

    return (
        <div className="chat-list-item" onClick={onClick}>
            <div className="chat-user">{receiverName}</div>
            <div className="chat-preview">{lastMessage}</div>
            <div className="chat-time">{lastMessageTime}</div>
        </div>
    );
};

export default ChatRoomListItem;