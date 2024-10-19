import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import MainPage from "./pages/MainPage";
import SignUpPage from "./pages/SignUpPage";
import FindPassword from "./pages/FindPassword";
import BookTradingMainPage from "./pages/BookTradingMainPage";
import ProductRegistrationPage from "./pages/ProductRegistrationPage";
import MyStorePage from "./pages/MyStorePage";
import ProductDetailPage from "./pages/ProductDetailPage";
import UserStorePage from "./pages/UserStorePage";
import RequireAuth from "./components/RequireAuth";
import SearchResultPage from "./pages/SearchResultPage";
import ChatRoomPage from "./pages/ChatRoomPage";
import CheckOutPage from "./pages/CheckOutPage";
import PaymentSuccess from "./pages/PaymentSuccessPage";
import PaymentFailure from "./pages/PaymentFailurePage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/SignUpPage" element={<SignUpPage />} />
        <Route path="/FindPassword" element={<FindPassword />} />
        <Route
          path="/BookTradingMainPage"
          element={
            <RequireAuth>
              <BookTradingMainPage />
            </RequireAuth>
          }
        />
        <Route
          path="/ProductRegistrationPage"
          element={
            <RequireAuth>
              <ProductRegistrationPage />
            </RequireAuth>
          }
        />
        <Route
          path="/MyStorePage"
          element={
            <RequireAuth>
              <MyStorePage />
            </RequireAuth>
          }
        />
        <Route
          path="/product/:productId"
          element={
            <RequireAuth>
              <ProductDetailPage />
            </RequireAuth>
          }
        />
        <Route
          path="/store/:storeId"
          element={
            <RequireAuth>
              <UserStorePage />
            </RequireAuth>
          }
        />
        <Route
          path="/search"
          element={
            <RequireAuth>
              <SearchResultPage />
            </RequireAuth>
          }
        />
        <Route
          path="/chat-room/:roomId?"
          element={
            <RequireAuth>
              <ChatRoomPage />
            </RequireAuth>
          }
        />
        <Route
          path="/ChatRoomPage"
          element={
            <RequireAuth>
              <ChatRoomPage />
            </RequireAuth>
          }
        />
        <Route
          path="/checkout"
          element={
            <RequireAuth>
              <CheckOutPage />
            </RequireAuth>
          }
        />
        <Route
          path="/payment-success"
          element={
            <RequireAuth>
              <PaymentSuccessPage />
            </RequireAuth>
          }
        />
        <Route
          path="/payment-failure"
          element={
            <RequireAuth>
              <PaymentFailurePage />
            </RequireAuth>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
