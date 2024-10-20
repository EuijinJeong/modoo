package com.example.modoo.controller;

import com.example.modoo.dto.PaymentVerifiedDto;
import com.example.modoo.entity.Member;
import com.example.modoo.service.PaymentService;
import com.example.modoo.service.UserEmailLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 결제 완료 후 아임포트 서버로부터 결제 정보 검증하기
     * 검증이 성공적으로 완료되면 자동적으로 결제가 완료된다.
     *
     * @param request : 검증 요청
     * @return : 검증 결과
     */
    @PostMapping("/payment/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, Object> request) {
        PaymentVerifiedDto paymentVerifiedDto = new PaymentVerifiedDto();
        paymentVerifiedDto.setImpUid((String) request.get("imp_uid"));
        paymentVerifiedDto.setAmount((Integer) request.get("amount"));
        Integer productIdInt = (Integer) request.get("productId");
        Long productIdLong = productIdInt.longValue();
        paymentVerifiedDto.setProductId(productIdLong);

        UserEmailLookupService userEmailLookupService = new UserEmailLookupService();
        String email = userEmailLookupService.getCurrentUserEmail();
        paymentVerifiedDto.setUserEmail(email);

        boolean isVerified = this.paymentService.verifiedAndSavePaymentInfo(paymentVerifiedDto);

        // 결제 유효성 검사에 성공한경우.
        if(isVerified) {
            return ResponseEntity.ok("결제 유효성 검사에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed");
        }
    }
}
