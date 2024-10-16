import React, { useState } from "react";
import "../css/user_dashboard_header.css";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios"; // Include CSS for styling

const UserDashboardHeader = () => {
  const [show, setShow] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const navigate = useNavigate();

  const toggleMenu = () => {
    setShow((prevShow) => !prevShow);
  };

  const handleSearch = async () => {
    if (searchQuery.trim() === "") return;

    try {
      const response = await axios.get(`/api/search?query=${searchQuery}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      const searchResponse = response.data;

      navigate("/search", { state: { results: searchResponse } });
    } catch (e) {
      console.error("Search failed", e);
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === "Enter") {
      handleSearch();
    }
  };

  const handleChatRoomClick = async () => {
    try {
      const token = localStorage.getItem("token");

      // 서버로 채팅방 목록 요청 보내기
      const response = await axios.get("/api/chat-room-lists", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      console.log("Chat rooms from server:", response.data);
      const chatRooms = response.data; // 서버에서 받은 채팅방 목록

      // 채팅방 목록 페이지로 이동하고 상태로 채팅방 목록 넘기기
      navigate("/ChatRoomPage", { state: { chatRooms } });
    } catch (error) {
      console.error("Error fetching chat rooms:", error);
    }
  };

  const handleLogout = async () => {
    try {
      // FIXME: 404 오류 발생
      const response = await axios.post(
        "/api/logout",
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`, // JWT 토큰을 헤더에 포함
          },
        }
      );

      // 로그아웃 요청에 대한 응답 확인
      if (response.status === 200) {
        console.log("로그아웃 요청이 성공적으로 처리되었습니다.");
        // jwt 토큰 삭제
        localStorage.removeItem("token");
        // 초기 페이지로 리디렉션하면서 히스토리 스택을 덮어쓰기
        navigate("/", { replace: true });
        console.log("jwt 토큰이 정상적으로 삭제되었습니다.");
      }
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  return (
    <header id="header" role="banner">
      <div className="header_inner">
        <div className="header_logo">
          <a href="/BookTradingMainPage">모두의 전공책</a>
        </div>
        <input
          type="text"
          className="search-bar"
          placeholder="전공책 이름, 출판사명 입력"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          onKeyPress={handleKeyPress}
        />
        <nav className={`header_nav ${show ? "show" : ""}`} role="navigation">
          <ul>
            <li>
              <Link to="/ProductRegistrationPage">판매하기</Link>
            </li>
            <li>
              <a href="/MyStorePage">내상점</a>
            </li>
            <li>
              <a href="/ChatRoomPage" onClick={handleChatRoomClick}>
                모두톡
              </a>
            </li>
            <li>
              <button onClick={handleLogout} className="logout-button">
                로그아웃
              </button>
            </li>
          </ul>
        </nav>
        <div
          className="header_nav_mobile"
          id="headerToggle"
          aria-controls="primary-menu"
          aria-expanded={show ? "true" : "false"}
          role="button"
          tabindex="0"
          onClick={toggleMenu}
        >
          <span></span>
        </div>
      </div>
    </header>
  );
};

export default UserDashboardHeader;
