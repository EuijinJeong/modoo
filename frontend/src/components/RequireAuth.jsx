import React, {useEffect, useState} from 'react';
import {Navigate, useLocation} from "react-router-dom";

const RequireAuth = ({ children }) => {
    const token = localStorage.getItem('token'); // 로컬 스토리지에서 JWT 토큰 가져오기
    const location = useLocation(); // 현재 위치 정보 가져오기
    const [isAuthenticated, setIsAuthenticated] = useState(!!token);

    useEffect(() => {
        const checkToken = () => {
            const token = localStorage.getItem('token');
            setIsAuthenticated(!!token);
        };

        // 페이지 로드 시 토큰 확인
        checkToken();

        // 주기적으로 토큰 확인 (1초마다)
        const interval = setInterval(checkToken, 1000);

        return () => clearInterval(interval); // 컴포넌트 언마운트 시 interval 제거
    }, []);

    if (!isAuthenticated) {
        return <Navigate to="/" replace state={{ from: location }} />;
    }

    return children;
};


export default RequireAuth;