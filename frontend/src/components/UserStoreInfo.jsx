import React, {useEffect, useState} from 'react';
import userIcon from "../assets/free-icon-font-user-3917705.png";

const UserStoreInfo = ( {storeId} ) => {
    const [storeName, setStoreName] = useState('');
    const [storeNote, setStoreNote] = useState('');

    useEffect(() => {

    }, []);

    return (
        <div className="store-info-container">
            <div className="store-info-left">
                <div className="store-image-placeholder">
                    <div className="store-icon">
                        <img src={userIcon} alt="UserIcon" />
                    </div>
                </div>
            </div>
            <div className="store-info-right">
                <div className="store-name-container">
                    <h2 className="store-name">{storeName}</h2>
                </div>
                <hr className="separator" />
                <div className="store-note-container">
                    <div>
                        <p className="store-note">{storeNote}</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default UserStoreInfo;