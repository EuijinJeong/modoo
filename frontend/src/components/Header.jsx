import React from "react";
import "../css/header.css";
const Header = () => {
  return (
    <header id="header" role="banner">
      <div className="header__inner">
        <div className="header__logo">
          <h1>
            <a href="/">모두의 전공책</a>
          </h1>
        </div>
      </div>
    </header>
  );
};

export default Header;
