import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import ProductItem from "./ProductItem";
import axios from "axios";
import "../css/shipping_form.css";

const ProductCheckOut = () => {
  const location = useLocation();
  const navigate = useNavigate();

  // ProductDetail에서 넘겨준 상품 정보를 받아옴
  const product = location.state?.product;

  // 배송지 정보를 관리하는 상태
  const [address, setAddress] = useState({
    recipient: "",
    phone: "",
    addressLine1: "",
    addressLine2: "",
    postalCode: "",
  });

  // 다음 우편번호 API 스크립트 로드
  useEffect(() => {
    const script = document.createElement("script");
    script.src =
      "https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
    script.async = true;
    document.body.appendChild(script);

    return () => {
      document.body.removeChild(script);
    };
  }, []);

  // 다음 우편번호 API 호출
  const handlePostcodeSearch = () => {
    new window.daum.Postcode({
      oncomplete: function (data) {
        // 도로명 주소, 지번 주소 등 원하는 주소 정보를 처리
        let fullAddress = data.address;
        let extraAddress = "";

        // 참고항목 있는 경우 추가
        if (data.addressType === "R") {
          // 법정동명이 있을 경우 추가한다. (법정리는 제외)
          // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
          if (data.bname !== "") {
            extraAddress += data.bname;
          }
          if (data.buildingName !== "") {
            extraAddress +=
              extraAddress !== ""
                ? ", " + data.buildingName
                : data.buildingName;
          }
          fullAddress += extraAddress !== "" ? " (" + extraAddress + ")" : "";
        }

        // 주소와 우편번호를 상태로 업데이트
        setAddress({
          ...address,
          addressLine1: fullAddress,
          postalCode: data.zonecode, // 우편번호
        });
      },
    }).open();
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAddress({
      ...address,
      [name]: value,
    });
  };

  // const handleSubmit = async (e) => {
  //   e.preventDefault();

  //   // 유효성 검사 (예: 필수 입력 필드가 비어 있는지 확인)
  //   if (
  //     !address.recipient ||
  //     !address.phone ||
  //     !address.addressLine1 ||
  //     !address.postalCode
  //   ) {
  //     alert("모든 필수 필드를 입력해 주세요.");
  //     return;
  //   }

  //   try {
  //     // 서버로 데이터 전송
  //     const token = localStorage.getItem("token"); // Authorization 토큰을 로컬 스토리지에서 가져옴
  //     const response = await axios.post(
  //       `/api/members/${member.id}/addresses`,
  //       address,
  //       {
  //         headers: {
  //           Authorization: `Bearer ${token}`,
  //           "Content-Type": "application/json",
  //         },
  //       }
  //     );
  //     console.log("Shipping data submitted:", response.data);
  //     alert("배송지 정보가 성공적으로 제출되었습니다.");
  //   } catch (error) {
  //     console.error("Error submitting shipping data:", error);
  //     alert("배송지 정보 제출 중 오류가 발생했습니다.");
  //   }

  //   // 여기서 address 데이터를 처리하거나 제출할 수 있음
  //   console.log("Shipping Address:", address);
  // };

  /**
   * 아임포트를 활용한 결제 처리 함수
   *
   * @returns {Promise<void>}
   */
  const handlePayment = async () => {
    // 유효성 검사
    if (
      !address.recipient ||
      !address.phone ||
      !address.addressLine1 ||
      !address.postalCode
    ) {
      alert("배송지 정보를 모두 입력해 주세요.");
      return;
    }
    console.log("일반 결제 시작");

    // 가맹점 식별하기
    const { IMP } = window; // 아임포트 SDK 초기화
    IMP.init("imp81518761"); // 아임포트 관리자 페이지에서 발급된 가맹점 식별코드

    // 결제 데이터 정의하기
    IMP.request_pay(
      {
        pg: "html5_inicis", // 사용할 PG사
        pay_method: "card", // 결제 수단
        merchant_uid: `mid_${new Date().getTime()}`, // 고유 주문번호
        name: product.title, // 결제할 상품 이름
        amount: product.price, // 결제 금액
        buyer_email: "buyer@example.com", // 구매자 이메일
        buyer_name: address.recipient, // 구매자 이름
        buyer_tel: address.phone, // 구매자 전화번호
        buyer_addr: `${address.addressLine1} ${address.addressLine2}`, // 구매자 주소
        buyer_postcode: address.postalCode, // 구매자 우편번호
      },
      async (response) => {
        if (response.success) {
          try {
            // 결제가 성공했을 때 서버로 검증 요청을 보냄
            const verifyResponse = await axios.post(
              "/api/payment/verify",
              {
                imp_uid: response.imp_uid, // 아임포트 결제 고유 ID
                amount: product.price, // 결제 금액
                productId: product.id, // 제품 아이디 번호.
              },
              {
                headers: {
                  Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
              }
            );

            if (verifyResponse.status === 200) {
              alert("결제가 성공적으로 검증되었습니다.");
              navigate("/payment-success"); // 검증 성공 시 성공 페이지로 이동
            } else {
              alert("결제 검증에 실패했습니다.");
            }
          } catch (error) {
            console.error("결제 검증 중 오류 발생:", error);
            alert("결제 검증 중 오류가 발생했습니다.");
          }
        } else {
          // 결제 실패 시 처리
          alert(`결제 실패: ${response.error_msg}`);
        }
      }
    );
  };

  if (!product) {
    return <div>상품 정보가 없습니다.</div>;
  }

  return (
    <div className="checkout-container">
      {/* 좌측: 배송지 정보 폼 */}
      <div className="shipping-form">
        <h2>배송지 정보</h2>
        <form>
          <label>수령인</label>
          <input
            type="text"
            name="recipient"
            value={address.recipient}
            onChange={handleChange}
            placeholder="수령인 이름"
          />

          <label>전화번호</label>
          <input
            type="text"
            name="phone"
            value={address.phone}
            onChange={handleChange}
            placeholder="전화번호"
          />

          <label>주소 1</label>
          <input
            type="text"
            name="addressLine1"
            value={address.addressLine1}
            onChange={handleChange}
            placeholder="주소"
          />

          <label>주소 2</label>
          <input
            type="text"
            name="addressLine2"
            value={address.addressLine2}
            onChange={handleChange}
            placeholder="상세 주소"
          />

          <label>우편번호</label>
          <input
            type="text"
            name="postalCode"
            value={address.postalCode}
            readOnly
          />

          <button type="button" onClick={handlePostcodeSearch}>
            주소 검색
          </button>
        </form>
      </div>

      {/* 우측: ProductItem 컴포넌트 재사용 */}
      <div className="product-summary">
        {product ? (
          <>
            <ProductItem product={product} />
            <button onClick={handlePayment} className="payment-button">
              결제하기
            </button>
          </>
        ) : (
          <p>상품 정보가 없습니다.</p>
        )}
      </div>
    </div>
  );
};

export default ProductCheckOut;
