package com.example.modoo.service;

import com.example.modoo.dto.OrderHistoryDto;
import com.example.modoo.dto.PaymentVerifiedDto;
import com.example.modoo.entity.Member;
import com.example.modoo.entity.OrderHistory;
import com.example.modoo.entity.Payment;
import com.example.modoo.entity.Product;
import com.example.modoo.repository.MemberRepository;
import com.example.modoo.repository.OrderHistoryRepository;
import com.example.modoo.repository.PaymentRepository;
import com.example.modoo.repository.ProductRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderHistoryRepository orderHistoryRepository;


    /**
     * 거래요청이 유효한지를 판단해주는 메서드이다.
     * @param paymentVerifiedDto : 결제 검증에 필요한 데이터를 담은 객체이다.
     * @return : 검증 결과를 boolean 형태로 돌려준다.
     */
    @Transactional
    public boolean verifiedAndSavePaymentInfo(PaymentVerifiedDto paymentVerifiedDto) {
        Product product = this.productRepository.findById(paymentVerifiedDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product is not found"));

        OrderHistoryDto orderHistoryDto = new OrderHistoryDto();
        orderHistoryDto.setProductId(paymentVerifiedDto.getProductId());
        orderHistoryDto.setImpUid(paymentVerifiedDto.getImpUid());
        orderHistoryDto.setAmount(paymentVerifiedDto.getAmount());
        orderHistoryDto.setUserEmail(paymentVerifiedDto.getUserEmail());

        // 정보 데이터베이스에 저장함.
        OrderHistory orderHistory = this.convertToOrderHistory(orderHistoryDto);
        // FIXME: 아래 값 null이라 오류가 발생함.
        orderHistoryRepository.save(orderHistory);

        return (double) paymentVerifiedDto.getAmount() == (double) product.getPrice();
    }

    @Transactional
    public void cancelPayment(String impUid) {
        Payment payment = paymentRepository.findByImpUid(impUid)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus("CANCELLED");
        payment.setCancelledAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    private OrderHistory convertToOrderHistory(OrderHistoryDto orderHistoryDto) {
        LocalDateTime localDateTime = LocalDateTime.now();
        // productId로 Product 객체를 조회
        Product product = productRepository.findById(orderHistoryDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderHistoryDto.getProductId()));

        Member member = memberRepository.findByEmail(orderHistoryDto.getUserEmail())
                .orElseThrow(() -> new RuntimeException("Member is not found"));

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setProduct(product);
        orderHistory.setAmount(orderHistoryDto.getAmount());
        orderHistory.setImpUid(orderHistoryDto.getImpUid());
        orderHistory.setCreatedAt(localDateTime);
        orderHistory.setMember(member);
        orderHistory.setStatus("PAID");

        System.out.println(orderHistory);

        return orderHistory;
    }
}
