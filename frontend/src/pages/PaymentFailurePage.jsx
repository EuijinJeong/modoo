import React from "react";
import { useLocation } from "react-router-dom";
import "../css/payment_failure.css";

/**
 * 결제 실패시 클라이언트에게 전달되는 페이지이다.
 * @returns
 */
const PaymentFailure = () => {
  const location = useLocation();
  const { errorMessage } = location.state || {};

  return (
    <div className="payment-failure-container">
      <h2>결제가 실패했습니다.</h2>
      <p>결제 처리 중 문제가 발생했습니다:</p>
      <p className="error-message">{errorMessage}</p>
      <p>다시 시도해 주시거나 고객 센터로 문의해 주세요.</p>
    </div>
  );
};

export default PaymentFailure;
