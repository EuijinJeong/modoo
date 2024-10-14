import React from 'react';
import '../css/sidebar.css'; // Include CSS for styling

const Sidebar = () => {
    return (
        <aside className="sidebar">
            <div className="saved-items">
                <h3>찜한상품</h3>
                <p>0</p>
            </div>
            <div className="recent-items">
                <h3>최근본상품</h3>
                <p>최근 본 상품이 없습니다.</p>
            </div>
        </aside>
    );
}

export default Sidebar;
