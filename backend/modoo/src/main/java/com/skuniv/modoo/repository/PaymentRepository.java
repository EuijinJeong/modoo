package com.skuniv.modoo.repository;

import com.skuniv.modoo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByImpUid(String impUid);
}
