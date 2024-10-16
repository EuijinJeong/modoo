import React, { useEffect, useState } from "react";
import ProductItem from "./ProductItem";
import MyProducts from "./MyProducts";
import "../css/my_shop_tap.css";
import axios from "axios";

const MyShopTap = () => {
  const [activeTab, setActiveTab] = useState("products");
  const [products, setProducts] = useState([]);
  const [storeId, setStoreId] = useState(null);
  const [isMyStore, setIsMyStore] = useState(false);

  useEffect(() => {
    const fetchStore = async () => {
      try {
        const response = await axios.get("/api/stores/current", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
        const store = response.data;
        setStoreId(store.id);
        console.log("상점 아이디를 잘 반환하는지 확인하는 코드 " + store.id);

        // 상점이 본인의 상점인지 확인하는 로직 추가
        const currentUserResponse = await axios.get("/api/stores/current", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
        const currentUserStore = currentUserResponse.data;
        if (currentUserStore.id === store.id) {
          setIsMyStore(true); // 본인 상점일 경우 true로 설정
        }
      } catch (error) {
        console.error("Error fetching store:", error);
      }
    };

    fetchStore();
  }, []);

  useEffect(() => {
    if (storeId !== null) {
      const fetchProducts = async () => {
        try {
          const response = await axios.get(`/api/products/store/${storeId}`, {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          });
          if (Array.isArray(response.data)) {
            setProducts(response.data);
          } else {
            console.error("Expected an array but got:", response.data);
          }
          console.log("제품 목록을 잘 반환하는지 확인하는 코드", response.data);
        } catch (error) {
          console.error("Error fetching products:", error);
        }
      };
      fetchProducts();
    }
  }, [storeId]);

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  //   return (
  //     <div>
  //       <div className="category-tabs">
  //         <button
  //           className={`tab ${activeTab === "products" ? "active" : ""}`}
  //           onClick={() => handleTabClick("products")}
  //         >
  //           상품 {products.length}
  //         </button>
  //         <button
  //           className={`tab ${activeTab === "wishlist" ? "active" : ""}`}
  //           onClick={() => handleTabClick("wishlist")}
  //         >
  //           찜
  //         </button>
  //       </div>
  //       {activeTab === "products" && (
  //         <div>
  //           <MyProducts products={products} storeId={storeId} />
  //         </div>
  //       )}
  //       {activeTab === "wishlist" && (
  //         <div className="wishlist-list">
  //           <p>찜한 상품 목록이 여기에 표시됩니다.</p>
  //         </div>
  //       )}
  //     </div>
  //   );
  return (
    <div>
      <div className="category-tabs">
        <button
          className={`tab ${activeTab === "products" ? "active" : ""}`}
          onClick={() => handleTabClick("products")}
        >
          상품 {products.length}
        </button>

        {/* 본인의 상점인 경우에만 찜 탭을 표시 */}
        {isMyStore && (
          <button
            className={`tab ${activeTab === "wishlist" ? "active" : ""}`}
            onClick={() => handleTabClick("wishlist")}
          >
            찜
          </button>
        )}
      </div>

      {activeTab === "products" && (
        <div>
          <MyProducts products={products} storeId={storeId} />
        </div>
      )}

      {activeTab === "wishlist" && isMyStore && (
        <div className="wishlist-list">
          <p>찜한 상품 목록이 여기에 표시됩니다.</p>
        </div>
      )}
    </div>
  );
};

export default MyShopTap;
