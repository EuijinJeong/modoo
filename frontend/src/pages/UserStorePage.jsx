import React from 'react';
import UserDashboardHeader from "../components/UserDashboardHeader";
import UserStoreInfo from "../components/UserStoreInfo";
import {useParams} from "react-router-dom";

const UserStorePage = () => {
    const {storeId} = useParams();
    return (
        <div>
            <UserDashboardHeader />
            <UserStoreInfo storeId={storeId} />
        </div>
    );
};

export default UserStorePage;