package com.example.modoo.service;

import com.example.modoo.dto.PaymentRequestDto;
import com.example.modoo.dto.PaymentResponseDto;
import com.example.modoo.dto.PaymentVerifiedDto;
import com.example.modoo.entity.Payment;
import com.example.modoo.entity.Product;
import com.example.modoo.repository.PaymentRepository;
import com.example.modoo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ProductRepository productRepository;


    /**
     * 거래요청이 유효한지를 판단해주는 메서드이다.
     * @param paymentVerifiedDto : 결제 검증에 필요한 데이터를 담은 객체이다.
     * @return : 검증 결과를 boolean 형태로 돌려준다.
     */
    @Transactional
    public boolean verifiedPayment(PaymentVerifiedDto paymentVerifiedDto) {

        // FIXME: PaymentVerifiedDto의 productId가 null로 들어옴.

        Product product = this.productRepository.findById(paymentVerifiedDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product is not found"));

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

    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
        // DTO를 엔티티로 변환하고 저장
        Payment payment = new Payment();
        payment.setImpUid(paymentRequestDto.getImpUid());
        payment.setAmount(paymentRequestDto.getAmount());
        payment.setStatus("PAID");
        payment.setCreatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        // 저장된 엔티티를 다시 DTO로 변환하여 반환
        // return PaymentResponseDTO.fromEntity(savedPayment);
        return null;
    }
}
