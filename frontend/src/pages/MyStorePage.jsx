import React from 'react';
import UserDashboardHeader from "../components/UserDashboardHeader";
import MyStoreInfo from "../components/MyStoreInfo";
import MyShopTap from "../components/MyShopTap";

const MyStorePage = () => {
    return (
        <div>
            <UserDashboardHeader />
            <MyStoreInfo />
            <MyShopTap />
        </div>
    );
};

export default MyStorePage;