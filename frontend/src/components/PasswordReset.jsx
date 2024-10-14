import React, { useState } from "react";
import "../css/password_reset.css";

const PasswordReset = () => {
  const [email, setEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [emailSent, setEmailSent] = useState(false);
  const [resetSuccessful, setResetSuccessful] = useState(false);

  /**
   *
   * @param e
   * @returns {Promise<void>}
   */
  const handleEmailSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem('token');
      if (!token) {
        console.error('토큰이 없습니다. 인증이 필요합니다.');
        return;
      }

      const response = await fetch("/api/password/reset-link", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email }),
      });
      // FIXME: 토큰 인증 오류 발생. (401)
      console.log(localStorage.getItem('token'));  // 콘솔에 토큰 출력
      if (response.ok) {
        alert("비밀번호 재설정 링크가 이메일로 발송되었습니다.");
        setEmailSent(true);
      } else {
        const errorMessage = await response.text();
        alert("Error: " + errorMessage);
      }
    } catch (error) {
      console.error("Error during sending reset link:", error);
    }
  };

  const handleResetSubmit = async (e) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
      return;
    }

    try {
      const response = await fetch("/api/password/reset", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email,        // 여기에 사용자의 이메일을 추가
          newPassword,  // 새로운 비밀번호
        }),
      });

      if (response.ok) {
        alert("비밀번호 재설정이 완료되었습니다.");
        setResetSuccessful(true);
      } else {
        const errorMessage = await response.text();
        alert("Error: " + errorMessage);
      }
    } catch (error) {
      console.error("Error during password reset:", error);
    }
  };


  if (resetSuccessful) {
    return (
      <div>
        <h2>Password Reset Successful!</h2>
      </div>
    );
  }

  return (
    <div>
      {!emailSent ? (
        <section id="find_password">
          <div className="form_inner">
            <form onSubmit={handleEmailSubmit}>
              <h2>비밀번호 재설정</h2>
              <label id="email-label" htmlFor="email">Email:</label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="이메일 양식에 맞게 입력하세요"
                required
              />
              <div id="button-container">
                <button type="submit">Send Reset Link</button>
              </div>
            </form>
          </div>
        </section>
      ) : (
          <section id="reset_password" >
            <div className="form_inner">
              <form onSubmit={handleResetSubmit}>
                <h2>Reset Your Password</h2>
                <label htmlFor="newPassword">New Password:</label>
                <input
                    type="password"
                    id="newPassword"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    required
                />
                <label htmlFor="confirmPassword">Confirm Password:</label>
                <input
                    type="password"
                    id="confirmPassword"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                />
                <button type="submit">Reset Password</button>
              </form>
            </div>

          </section>

      )}
    </div>
  );
};

export default PasswordReset;
