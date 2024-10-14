import axios from "axios";

const axiosInstance = axios.create({
    baseURL: "/api",
    headers: {
        "Content-Type": "application/json",
    },
    withCredentials: true,
});

// 서버에서 CSRF 토큰을 가져와서 설정 (csrf 비활성화시 필요 없음)
// const csrfTokenMeta = document.querySelector("meta[name='_csrf']");
// if (csrfTokenMeta) {
//     const csrfToken = csrfTokenMeta.content;
//     axiosInstance.defaults.headers.common['X-XSRF-TOKEN'] = csrfToken;
// }

export default axiosInstance;