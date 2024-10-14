import React, {useEffect, useState} from 'react';
import ChatRoomList from "./ChatRoomList";
import ChatRoom from "./ChatRoom";
import "../css/chat_room_wrapper.css"

/**
 *
 *
 * @param storeId
 * @param roomId
 * @returns {Element}
 * @constructor
 */
const ChatRoomWrapper = ({ storeId, roomId }) => {
    const [selectedChat, setSelectedChat] = useState(null);

    useEffect(() => {
        // 필요한 초기화 작업
    }, [storeId]);

    const handleChatSelect = (chat) => {
        setSelectedChat(chat);
        console.log("선택된 채팅방의 고유 아이디값: ", roomId);
    }

    return (
        <div>
            <ChatRoomList storeId={storeId} setSelectedChat={setSelectedChat} />
            {selectedChat && (
                <ChatRoom roomId={selectedChat.id} />
            )}
        </div>
    );
};

export default ChatRoomWrapper;