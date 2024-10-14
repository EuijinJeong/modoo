import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import "../css/ProductRegistration.css"
import axios from "axios";

const ProductRegistration = () => {

    const navigate = useNavigate();

    const [images, setImages] = useState([]);
    const [title, setTitle] = useState('');
    const [condition, setCondition] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [acceptOffers, setAcceptOffers] = useState(false);

    const handleImageChange = (e) => {
        if (e.target.files.length > 0) {
            const newFiles = Array.from(e.target.files);
            setImages([...images, ...newFiles]);
        }
    };

    const handleTitleChange = (e) => {
        setTitle(e.target.value);
    };

    const handleConditionChange = (e) => {
        setCondition(e.target.value);
    };

    const handleDescriptionChange = (e) => {
        setDescription(e.target.value);
    };

    const handlePriceChange = (e) => {
        setPrice(e.target.value);
    };

    const handleAcceptOffersChange = (e) => {
        setAcceptOffers(e.target.checked);
    };

    /**
     * 유효성 검사 메서드
     *
     * @param title
     * @param condition
     * @param price
     * @returns {boolean}
     */
    const isValidInput = (title, condition, price) => {
        // 각 필수 입력란이 비어있는지 확인
        if(!title || !condition || !price){
            alert("모든 필수값들을 입력해주세요.")
            return false;
        }
    }

    /**
     *
     * @param e
     * @returns {Promise<void>}
     */
    const handleSubmit = async (e) => {
      e.preventDefault();

      if(!(isValidInput(title, condition, price))){
          // 유효하지 않은 로직 처리
          alert("필수 입력란이 비어있습니다 확인해주세요.")
      }

      if (images.length === 0) {
          alert("최소한 하나의 이미지를 업로드해야 합니다.");
          return;
      }

        const product = {
            title: title,
            condition: condition,
            description: description,
            price: parseFloat(price), // 가격을 숫자로 변환
            acceptOffers: acceptOffers,
        };

        const formData = new FormData();
        formData.append('product', new Blob([JSON.stringify(product)], { type: 'application/json' }));

        images.forEach((image, index) => {
            formData.append('files', image);
        });

        // 로컬스토리지에 있는 사용자 인증 토큰 가져오기
        const token = localStorage.getItem('token');
        console.log('Sending token:', token);

        try{
            // 서버로 데이터 전송
            const response = await axios.post("http://localhost:8080/api/productRegistration" , formData,{
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${token}`
                }
            });

            if(response.status === 200){
                console.log('상품 등록 성공:', response.data);
                alert("상품등록을 완료하였습니다.")
                navigate('/BookTradingMainPage')
            }
        } catch (e) {
            alert("상품 등록 과정에서 서버문제로 오류가 발생하였습니다.")
            console.error('상품 등록 오류:', e);
        }
    };

    return (
        <div className="product-registration">
            <h1>상품 정보</h1>
            <div className="separator"></div>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>상품 이미지</label>
                    <input type="file" multiple onChange={handleImageChange} />
                </div>
                <div className="form-group">
                    <label>상품명</label>
                    <input type="text" value={title} placeholder="상품명을 입력해 주세요." onChange={handleTitleChange}/>
                </div>
                <div className="form-group">
                    <label>상품상태</label>
                    <div className="radio-group">
                        <label>
                            <input
                            type="radio"
                            value="new"
                            checked={condition === 'new'}
                            onChange={handleConditionChange}/>
                            <span>미개봉 상품</span>
                        </label>
                        <label>
                            <input
                                type="radio"
                                value="used"
                                checked={condition === 'used'}
                                onChange={handleConditionChange}/>
                            <span>중고 상품</span>
                        </label>
                    </div>
                </div>
                <div className="form-group">
                    <label>상품설명</label>
                    <textarea value={description} placeholder="상품 설명을 최대한 자세하게 적어주세요." onChange={handleDescriptionChange}></textarea>
                </div>
                <div className="form-group">
                    <label>가격</label>
                    <input type="number" value={price} placeholder="가격을 입력해 주세요." onChange={handlePriceChange} />
                </div>
                <div className="checkbox-container">
                    <label className="checkbox-label">
                        <input type="checkbox"
                               checked={acceptOffers}
                               onChange={handleAcceptOffersChange} />
                        <span>가격 제안 받기</span>
                    </label>
                </div>
                <div className="form-actions">
                    <button type="submit">등록하기</button>
                </div>
            </form>
        </div>
    );
};

export default ProductRegistration;