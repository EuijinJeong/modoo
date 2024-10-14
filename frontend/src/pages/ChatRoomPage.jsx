import React from 'react';
import UserDashboardHeader from "../components/UserDashboardHeader";
import {useParams} from "react-router-dom";
import ChatRoomWrapper from "../components/ChatRoomWrapper";

const ChatRoomPage = () => {
    const { storeId , roomId} = useParams();

    return (
        <div>
            <UserDashboardHeader />
            <ChatRoomWrapper storeId={storeId} roomId={roomId} />
        </div>
    );
};

export default ChatRoomPage;