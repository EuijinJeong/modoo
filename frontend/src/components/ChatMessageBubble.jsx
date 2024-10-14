import React, {useEffect} from 'react';
import "../css/chat_message_bubble.css"

const ChatMessageBubble = ({ message}) => {

    useEffect(() => {
        console.log('Messages state:', message);
    }, [message]);

    console.log('Rendering message:', message); // 추가

    return (
        <div className="chat-bubble">
            <p>{message}</p>
        </div>
    );
};

export default ChatMessageBubble;