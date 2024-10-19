import React from "react";
import "../css/order_history_card.css";

const OrderHistoryCard = () => {
  return (
    <div>
      <div className="flex-box">
        <div className="card-box">
          <div className="product-info-area">
            <div className="product-img">

            </div>
            <div className="product-info">
              <div className="product-name">상품명</div>
              <div className="product-price">상품 가격</div>
              <div className="buy-date">구매날짜</div>
            </div>
            <div className="btn-area">
              <button>구매확정</button>
              <div className="multiple-btn">
                <button>판매자 문의</button>
                <button>상점 방문</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderHistoryCard;
