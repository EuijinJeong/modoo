import React, { useEffect, useState } from "react";
import "../css/product_item.css";
import { Link } from "react-router-dom";

const ProductItem = ({ product, isUserProduct }) => {
  if (!product.fileUrls || product.fileUrls.length === 0) {
    console.log("No file URLs available for product:", product.title);
    return <div>No image available</div>;
  }

  const imageUrl = `http://localhost:8080/images/${product.fileUrls[0]}`;
  console.log("Product fileUrls: ", product.fileUrls); // product.fileUrls가 무엇인지 확인
  console.log("Image URL: ", imageUrl); // 실제 이미지 URL이 올바른지 확인

  return (
    <div className="product-item">
      <Link to={`/product/${product.id}`} className="product-link">
        <img src={imageUrl} alt={product.title} className="product-image" />
        <div className="product-details">
          <h2 className="product-title">{product.title}</h2>
          <p className="product-price">{product.price.toLocaleString()} 원</p>
        </div>
        {isUserProduct && <p className="user-product-label">내 상품</p>}
      </Link>
    </div>
  );
};

export default ProductItem;
