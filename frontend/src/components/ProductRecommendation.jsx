import React, {useEffect, useState} from 'react';
import ProductItem from './ProductItem';
import '../css/product_recommendation.css';
import axios from "axios"; // Include CSS for styling


const ProductRecommendation = () => {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        const fetchRandomProducts = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await axios.get('/api/products/random', {

                    headers : {
                        Authorization: `Bearer ${token}`
                    },
                    params: {count: 5}
                });
                setProducts(response.data);
            } catch (error) {
                console.error('Failed to fetch random products', error);
            }
        }
        fetchRandomProducts();
    }, []);

    return (
        <section className="product-recommendation">
            <h2>오늘의 상품 추천</h2>
            <div className="product-list">
                {products.length > 0 ? (
                    products.map(product => (
                        <ProductItem key={product.id} product={product} />
                    ))
                ) : (
                    <p>추천 상품이 없습니다.</p>
                )}
            </div>
        </section>
    );
}

export default ProductRecommendation;
