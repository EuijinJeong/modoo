package com.example.modoo.repository;

import com.example.modoo.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    /**
     *
     * @param id
     * @return
     */
    Optional<OrderHistory> findById(Long id);
}
