import React, {useState} from "react";
import axiosInstance from "../api/axiosInstance";
import { useNavigate } from 'react-router-dom';
import {Link} from "react-router-dom";
import "../pages/SignUpPage";
import "../pages/FindPassword";
import "../css/introduce.css";


const Introduce = () => {
    // 이메일과 비밀번호를 위한 상태 변수 설정
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    // 아이디 입력시 상태 업데이트
    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    };

    // 비밀번호 입력시 상태 업데이트
    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    };

    // 유효성 검사 함수
    const isValidInput = (email, password) => {
        console.log("Validating:", { email, password }); // 입력 값 로깅
        // 아이디와 비밀번호 입력란이 공백인지 확인
        if (!email || !password) {
            alert("이메일과 비밀번호를 모두 입력해주세요.");
            return false;
        }
        // 이메일 형식 유효성 검사 조건
        if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)) {
            alert("유효하지 않은 이메일 형식입니다.");
            return false;
        }
        // 비밀먼호 형식 유효성 검사 조건 (영어, 숫자, 특수문자로 8글자 이상 조합)
        if (
            !/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/.test(
                password
            )
        ) {
            alert(
                "비밀번호는 8글자 이상이어야 하며, 영문, 숫자, 특수문자를 모두 포함해야 합니다."
            );
            return false;
        }
        // 모든 조건을 통과하면 유효한 입력으로 간주
        return true;
    }

    // 로그인 폼 제출시 처리 함수
    const handleSubmit = async (e) => {
        e.preventDefault();

        // 유효성 검사 로직 작성
        if (!isValidInput(email, password)) {
            return; // 유효하지 않은 경우 폼 제출을 중단
        }

        try {
            const response = await axiosInstance.post("/signIn", {
                email,
                password
            });

            console.log('서버 응답:', response.data);

            if (response.status === 200 && response.data.accessToken) {
                const token = response.data.accessToken; // 서버에서 받은 JWT 토큰
                localStorage.setItem('token', token); // 로컬 스토리지에 토큰 저장
                console.log('토큰 저장 완료:', token);
                navigate('/BookTradingMainPage', {replace: true});  // 거래 메인 페이지로 이동
            } else {
                console.error("서버 응답 오류:", response.status);
                alert("서버 응답 오류가 발생했습니다. 다시 시도해주세요.");
            }
        } catch (error) {
            console.error("로그인 요청 중 오류 발생:", error);
            alert("로그인 요청 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
        // 폼 초기화
        setEmail("");
        setPassword("");
    };

    return (
        <>
            <section id="introduce">
                <div className="intro_inner">
                    <h1 className="intro_title">전공책을 더욱 저렴하게, 더욱 편리하게</h1>
                    <h4 className="sub_title">
                        모두의 전공책은 학교 전공책을 중고 거래할 수 있는 플랫폼으로,{" "}
                        <br></br>학생들 간에 쉽고 효율적으로 전공책을 구매하고 판매할 수
                        있는 환경을 제공합니다.
                    </h4>
                </div>
                <div className="login">
                    <h2 id ="login_logo" className="start">모두의 전공책</h2>
                    <form onSubmit={handleSubmit}>
                        <input
                            type="text"
                            placeholder="이메일"
                            value={email}
                            onChange={handleEmailChange}
                        ></input>
                        <input
                            type="password"
                            placeholder="비밀번호"
                            value={password}
                            onChange={handlePasswordChange}
                        ></input>
                        <button type="submit" className="sign_in">
                            시작하기
                        </button>
                        <div className="signup_container">
                            <p>
                                회원이 아니신가요? <Link to="/SignUpPage">SignUp</Link>
                            </p>
                            <p>
                                비밀번호를 잃어버리셨나요?{" "}
                                <Link to="/FindPassword">Find Password</Link>
                            </p>
                        </div>
                    </form>
                </div>
            </section>
        </>
    );
};
export default Introduce;
