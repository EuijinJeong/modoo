import React, {useEffect, useState} from 'react';
import axios from "axios";
import ChatRoomListItem from "./ChatRoomListItem";


/**
 * 서버에서 받아온 채팅방 리스트를 표시하고,
 * 방을 클릭하면 setSelectedChat을 호출해서 상위 컴포넌트에 선택된 방을 알려준다.
 *
 * @param storeId
 * @param setSelectedChat
 * @returns {Element}
 * @constructor
 */
const ChatRoomList = ({ storeId , setSelectedChat }) => {
    const [chatRooms, setChatRooms] = useState([]);

    // 서버에 채팅룸 리스트를 불러오는 요청
    useEffect(() => {
        const fetchChatRooms = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await axios.get('/api/chat-room-lists', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                console.log("Chat room List response:", response.data);  // 데이터 확인
                setChatRooms(response.data);
            } catch (error) {
                console.error("Chatroom List를 서버에서 불러오는 과정에서 오류가 발생했습니다.", error);
            }
        }

        fetchChatRooms();
    }, []);

    return (
        <div className="chat-room-list">
            <h2>채팅방 목록</h2>
            <ul>
                {chatRooms.length > 0 ? (
                    chatRooms.map((room) => (
                        <li key={room.id}>
                            <ChatRoomListItem
                                room={room}
                                onClick={() => setSelectedChat(room)}
                            />
                        </li>
                    ))
                ) : (
                    <li>채팅방이 없습니다.</li>
                )}
            </ul>
        </div>
    );
};

export default ChatRoomList;