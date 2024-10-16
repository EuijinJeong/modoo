import React, { useEffect, useState } from "react";
import axios from "axios";
import userIcon from "../assets/free-icon-font-user-3917705.png";
import MyProducts from "./MyProducts";

const UserStoreInfo = ({ storeId }) => {
  const [storeName, setStoreName] = useState("");
  const [storeNote, setStoreNote] = useState("");
  const [products, setProducts] = useState([]);

  useEffect(() => {
    const fetchStoreInfo = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(`/api/stores/${storeId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        // 상점 정보를 성공적으로 가져왔을 때, storeName과 storeNote를 설정
        setStoreName(response.data.name);
        setStoreNote(response.data.note);

        // 제품 정보 요청
        const productsResponse = await axios.get(
          `/api/products/store/${storeId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        setProducts(productsResponse.data); // 제품 정보를 상태에 저장
      } catch (error) {
        console.error("Error fetching store info:", error);
      }
    };

    if (storeId) {
      fetchStoreInfo();
    }
  }, [storeId]);

  return (
    <div>
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
            <div>
              <p className="store-note">{storeNote}</p>
            </div>
          </div>
        </div>
      </div>
      <div className="store-products-container">
        <MyProducts products={products} storeId={storeId} />{" "}
        {/* products와 storeId를 MyProducts에 전달 */}
      </div>
    </div>
  );
};

export default UserStoreInfo;
