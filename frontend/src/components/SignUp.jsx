import React, { useState } from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import "../pages/MainPage";
import "../css/signup.css";
import axiosInstance from "../api/axiosInstance";

const SignUp = () => {
  // 회원가입 폼의 상태 관리
  const [email, setEmail] = useState("");
  const [studentId, setStudentId] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [fullName, setFullName] = useState("");

  const navigate = useNavigate();
  // 이메일 입력 시 상태 변경 이벤트 핸들러
  const handleEmailChange = (event) => {
    setEmail(event.target.value);
  };

  // 비밀번호 입력 시 상태 변경 이벤트 핸들러
  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  // 비밀번호 확인 입력 시 상태 변경 이벤트 핸들러
  const handleConfirmPasswordChange = (event) => {
    setConfirmPassword(event.target.value);
  };

  // 학번 입력 시 상태 변경 이벤트 핸들러
  const handleStudentIdChange = (event) => {
    setStudentId(event.target.value);
  };

  // 이름 입력 시 상태 변경 이벤트 핸들러
  const handleFullNameChange = (event) => {
    setFullName(event.target.value);
  };

  // 입력한 데이터를 객체에 담기
  const formData = {
    email,
    password,
    fullName,
    studentId,
  };

  // 유효성 검사 함수
  const isValidInput = (email, password, confirmPassword, fullName) => {
    // 각 입력값이 비어 있는지 확인
    if (!email || !password || !confirmPassword || !fullName) {
      alert("모든 필드를 입력해주세요.");
      return false;
    }
    // 이메일 형식 유효성 검사 조건
    if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)) {
      alert("유효하지 않은 이메일 형식입니다.");
      return false;
    }
    // 비밀먼호 형식 유효성 검사 조건
    if (
      !/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/.test(
        password
      )
    ) {
      // 비밀번호는 8글자 이상이어야 하고, 영문, 숫자, 특수문자를 모두 포함해야 합니다.
      alert(
        "비밀번호는 8글자 이상이어야 하며, 영문, 숫자, 특수문자를 모두 포함해야 합니다."
      );
      return false;
    }

    // 비밀번호와 비밀번호 확인이 일치하는지 확인
    if (password !== confirmPassword) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      return false;
    }

    // 모든 조건을 통과하면 유효한 입력으로 간주
    return true;
  };

  const handleCheckEmailDuplicate = async () => {
    try {
      // 이메일 중복 여부를 확인하는 API 요청
      const response = await axiosInstance.get(
        `/check-email-duplicate?email=${email}`
      );

      if (response.data.isDuplicate) {
        alert("이미 사용 중인 이메일입니다.");
      } else {
        alert("사용 가능한 이메일입니다.");
      }
    } catch (error) {
      console.error("이메일 중복 확인 중 오류 발생:", error);
      alert("이메일 중복 확인 중 오류가 발생했습니다.");
    }
  };

  // 회원가입 폼 제출 이벤트 핸들러
  const handleSubmit = async (e) => {
    e.preventDefault();
    // 여기에 회원가입 처리 로직을 작성합니다.
    // 유효성 검사
    const isValid = isValidInput(email, password, confirmPassword, fullName);
    if (!isValid) {
      // 유효하지 않은 로직 처리
      alert("입력한 정보가 올바르지 않습니다. 다시 확인해주세요.");
    }
    try {
      // 서버로 데이터 전송
      const response = await axiosInstance.post("/signup", {
        email,
        fullName,
        password,
        studentId,
      });

      // 서버에서 유효성 검사 및 회원가입 처리 결과 피드백을 받음
      if (response.status === 200) {
        // 회원가입이 성공한 경우
        alert("회원가입이 완료되었습니다.");
        navigate("/"); // 거래 메인페이지로 이동
      } else if (response.status === 409) {
        // 이미 존재하는 이메일 주소인 경우
        alert("이미 존재하는 이메일 주소입니다.");
      } else {
        // 기타 서버 응답 오류 처리
        alert(response.data.message || "회원가입에 실패하였습니다.");
      }
    } catch (error) {
      console.error("회원가입 요청 중 오류 발생:", error);
      alert("회원가입 요청 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  return (
    <section id="sign_up">
      <div className="signup_inner">
        <h2>
          회원가입을 위해<br></br>정보를 입력해주세요.
        </h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email:</label>
            <input
              type="text"
              id="email"
              value={email}
              onChange={handleEmailChange}
              placeholder="이메일을 입력해주세요"
              required
            />
            <button type="button" onClick={handleCheckEmailDuplicate}>
              중복확인
            </button>
          </div>
          <div className="form-group">
            <label htmlFor="fullName">이름:</label>
            <input
              type="text"
              id="fullName"
              value={fullName}
              onChange={handleFullNameChange}
              placeholder="이름을 입력해주세요"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="studentId">학번:</label>
            <input
              type="text"
              id="studentId"
              value={studentId}
              onChange={handleStudentIdChange}
              placeholder="학번을 입력해주세요"
              required
            ></input>
          </div>
          <div className="form-group">
            <label htmlFor="password">비밀번호:</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={handlePasswordChange}
              placeholder="비밀번호 입력 (문자, 숫자, 특수문자 포함 8-20자)"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="confirmPassword">비밀번호 확인:</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={handleConfirmPasswordChange}
              placeholder="비밀번호 재입력"
              required
            />
          </div>
          <form>
            <label htmlFor="agree" className="agree-label">
              <input type="checkbox" className="agree" />
              이용약관 개인정보 수집 및 정보이용에 동의합니다.
            </label>
          </form>
          <button type="submit">가입하기</button>
          <div className="additional-links">
            <p>
              이미 회원이신가요? <Link to="/">Login</Link>
            </p>
          </div>
        </form>
      </div>
    </section>
  );
};

export default SignUp;
