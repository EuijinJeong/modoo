import React from 'react';
import ProductDetail from "../components/ProductDetail";
import {useParams} from "react-router-dom";
import UserDashboardHeader from "../components/UserDashboardHeader";

const ProductDetailPage = () => {
    const { productId } = useParams();

    return (
        <div>
            <UserDashboardHeader />
            <ProductDetail productId={productId} />
        </div>
    );
};

export default ProductDetailPage;