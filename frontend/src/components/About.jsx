import React from "react";
import "../css/about.css";

/**
 * 이 사이트의 기능을 소개하는 영역
 */

const About = () => {
  return (
    <section id="about">
      <div className="about_inner">
        <h2 className="about_title">About Us</h2>
        <div className="about_desc">
          <div>
            <div className="title-container">
              <span className="section-number">1.</span>
              <h3>경매 및 거래 기능</h3>
            </div>
            <p>
              우리의 사이트는 간편한 경매 및 거래 기능을 제공합니다. <br></br>학생들은
              필요한 전공책을 등록하여 경매에 올릴 수 있으며,<br></br>다른 사용자들이
              참여하여 책을 입찰할 수 있습니다. <br></br>또한, 미리 정해진
              가격으로 바로 거래할 수도 있어서 더욱 편리합니다.
            </p>
          </div>
          <div>
            <div className="title-container">
              <span className="section-number">2.</span>
              <h3>검색 및 필터링 기능</h3>
            </div>
            <p>
              다양한 검색 및 필터링 기능을 제공하여 사용자가 원하는 책을 쉽게
              찾을 수 있습니다. <br></br>검색어를 입력하거나 카테고리, 가격대 등
              다양한 조건으로 책을 필터링할 수 있습니다. <br></br>이를 통해
              사용자들은 자신에게 가장 적합한 책을 빠르게 찾을 수 있습니다.
            </p>
          </div>
          <div>
            <div className="title-container">
              <span className="section-number">3.</span>
              <h3>커뮤니티 및 평가 기능</h3>
            </div>
            <p>
              사용자들은 경험을 공유하고 질문을 하며 서로의 정보를 나눌 수
              있습니다. <br></br>또한, 판매자와 구매자 간에 거래 후 평가를 남길 수 있어서
              신뢰할 수 있는 거래를 할 수 있습니다. <br></br>이를 통해 더욱
              안전하고 투명한 거래가 이루어집니다.
            </p>
          </div>
        </div>
      </div>
    </section>
  );
};

export default About;
