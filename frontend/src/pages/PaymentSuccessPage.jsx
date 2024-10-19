import React from "react";
import { useLocation } from "react-router-dom";
import "../css/payment_success.css";

/**
 * 결제 성공시 클라이언트에게 전달되는 페이지이다.
 * @returns
 */
const PaymentSuccess = () => {
  const location = useLocation();
  const { product, amount } = location.state || {}; // 전달받은 상품 정보와 금액

  return (
    <div className="payment-success-container">
      <h2>결제가 성공적으로 완료되었습니다!</h2>
      <div className="payment-info">
        <p>
          <strong>상품명:</strong> {product?.title}
        </p>
        <p>
          <strong>결제 금액:</strong> {amount} 원
        </p>
        <p>
          <strong>결제 일시:</strong> {new Date().toLocaleString()}
        </p>
      </div>
      <p>구매해 주셔서 감사합니다. 빠른 시일 내에 배송해드리겠습니다!</p>
    </div>
  );
};

export default PaymentSuccess;
