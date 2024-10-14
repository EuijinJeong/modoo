import React from 'react';
import ProductItem from "./ProductItem";
import "../css/product_item.css"

const MyProducts = ({products, storeId}) => {
    return (
        <div className="product-list">
            {products && products.length > 0 ? (
                products.map(product => (
                    product && product.id ? (
                        <ProductItem
                            key={product.id}
                            product={product}
                            isUserProduct={product.storeId === storeId} // storeId 비교
                        />
                    ) : null
                ))
            ) : (
                <div>Loading products...</div>
            )}
        </div>
    );
};

export default MyProducts;