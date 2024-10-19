package com.example.modoo.controller;

import com.example.modoo.dto.PaymentRequestDto;
import com.example.modoo.dto.PaymentVerifiedDto;
import com.example.modoo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 결제 완료 후 아임포트 서버로부터 결제 정보 검증하기
     * @param request
     * @return
     */
    @PostMapping("/payment/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, Object> request) {
        PaymentVerifiedDto paymentVerifiedDto = new PaymentVerifiedDto();
        paymentVerifiedDto.setImpUid((String) request.get("imp_uid"));
        paymentVerifiedDto.setAmount((Integer) request.get("amount"));
        Integer productIdInt = (Integer) request.get("productId");
        Long productIdLong = productIdInt.longValue();
        paymentVerifiedDto.setProductId(productIdLong);

        boolean isVerified = this.paymentService.verifiedPayment(paymentVerifiedDto);

        // 결제 유효성 검사에 성공한경우.
        if(isVerified) {
            return ResponseEntity.ok("결제 유효성 검사에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed");
        }
    }


}
