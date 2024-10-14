import React from 'react';
import UserDashboardHeader from '../components/UserDashboardHeader';
import ProductRecommendation from '../components/ProductRecommendation';
import Sidebar from '../components/Sidebar';

const BookTradingMainPage = () => {
    return (
        <div className="main-page">
            <UserDashboardHeader />
            <div className="content">
                <ProductRecommendation />
            </div>
        </div>
    );
}

export default BookTradingMainPage;
