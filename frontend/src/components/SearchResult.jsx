import React from 'react';
import ProductItem from '../components/ProductItem';
import {useLocation} from "react-router-dom";
import "../css/search_result.css"

const SearchResult = () => {
    const location = useLocation();
    const { results } = location.state || { results: [] };

    return (
        <div className="search-results-container">
            <h1>검색 결과</h1>
            <div className="search-results">
                {results.length > 0 ? (
                    results.map((product) => (
                        <ProductItem key={product.id} product={product} isUserProduct={false} />
                    ))
                ) : (
                    <p>검색 결과가 없습니다.</p>
                )}
            </div>
        </div>
    );
};

export default SearchResult;