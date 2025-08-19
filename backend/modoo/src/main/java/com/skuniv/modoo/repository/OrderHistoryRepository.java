package com.skuniv.modoo.repository;

import com.skuniv.modoo.entity.OrderHistory;
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
