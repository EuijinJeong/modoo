import React, { useEffect, useState } from "react";
import "../css/user_shop_info.css";
import userIcon from "/Users/jeong-uijin/teamsphere/frontend/src/assets/free-icon-font-user-3917705.png";
import axios from "axios";

/**
 * @returns {Element}
 * @constructor
 *
 * 내 상점의 상단부분에 존재하는 컴포넌트
 */

const MyStoreInfo = () => {
  const [storeName, setStoreName] = useState("");
  const [storeNote, setStoreNote] = useState("상점 소개글을 작성해주세요.");
  const [editingNote, setEditingNote] = useState(false);
  const [newStoreNote, setNewStoreNote] = useState(storeNote);

  useEffect(() => {
    axios
      .get("/api/stores/current", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      })
      .then((response) => {
        const store = response.data; // 서버에서 반환된 store 객체
        console.log("Store data fetched:", store); // 잘 반환함
        setStoreName(store.name); // Store 객체의 name 값을 상태로 설정
        setStoreNote(store.note); // Store 객체의 note 값을 상태로 설정
        setNewStoreNote(store.note); // Edit할 때 기존 값을 기본값으로 설정
      })
      .catch((error) => {
        console.error("There was an error fetching the store data!", error);
      });
  }, []);

  const handleEditNoteClick = () => {
    setEditingNote(true);
  };

  const handleSaveNoteClick = async () => {
    try {
      const response = await axios.post(
        "/api/stores/update-store-note",
        { note: newStoreNote },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`, // JWT 토큰을 헤더에 포함
          },
        }
      );
      if (response.status === 200) {
        setStoreNote(newStoreNote);
        setEditingNote(false);
      }
    } catch (error) {
      console.error("There was an error updating the store note!", error);
    }
  };

  const handleNoteChange = (e) => {
    setNewStoreNote(e.target.value);
  };

  return (
    <div className="store-info-container">
      <div className="store-info-left">
        <div className="store-image-placeholder">
          <div className="store-icon">
            <img src={userIcon} alt="UserIcon" />
          </div>
        </div>
      </div>
      <div className="store-info-right">
        <div className="store-name-container">
          <h2 className="store-name">{storeName}</h2>
        </div>
        <hr className="separator" />
        <div className="store-note-container">
          {editingNote ? (
            <div className="edit-container">
              <textarea
                value={newStoreNote}
                onChange={handleNoteChange}
                className="edit-textarea"
              />
              <button onClick={handleSaveNoteClick} className="save-btn">
                확인
              </button>
            </div>
          ) : (
            <div>
              <p className="store-note">{storeNote}</p>
              <button className="edit-intro-btn" onClick={handleEditNoteClick}>
                소개글 수정
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default MyStoreInfo;
