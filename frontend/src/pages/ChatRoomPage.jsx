import React from "react";
import UserDashboardHeader from "../components/UserDashboardHeader";
import { useParams } from "react-router-dom";
import ChatRoomWrapper from "../components/ChatRoomWrapper";

const ChatRoomPage = () => {
  const { roomId } = useParams();

  return (
    <div>
      <UserDashboardHeader />
      <ChatRoomWrapper roomId={roomId} />
    </div>
  );
};

export default ChatRoomPage;
