import React, {useState} from 'react';
import "../css/chat_message_input.css";

const ChatMessageInput = ({ onSendMessage }) => {
    const [message, setMessage] = useState('');

    const handleChange = (e) => {
        setMessage(e.target.value);
    };

    const handleSend = () => {
        if(message.trim()) {
            onSendMessage(message);
            console.log('Message sent: ', message);
            setMessage(''); // 전송 후 입력창 비우기
        }
    };

    const handleKeyPress = (e) => {
        if(e.key === 'Enter') {
            handleSend();
        }
    };

    return (
        <div className="chat-message-input">
            <input
                type="text"
                value={message}
                onChange={handleChange}
                onKeyPress={handleKeyPress}
                placeholder="메세지를 입력해 주세요."
                className="chat-input-field"
            />
            <button onClick={handleSend} className="chat-send-button">
                Send
            </button>
        </div>
    );
};

export default ChatMessageInput;