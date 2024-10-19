package com.example.modoo.repository;

import com.example.modoo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);

    // 상점 ID로 제품 목록을 가져오는 메서드 추가
    List<Product> findByStoreId(Long storeId);
    List<Product> findByTitleContaining(String query);
    // 무작위로 상품을 가져오는 쿼리
    @Query(value = "SELECT * FROM product ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Product> findRandomProducts(@Param("count") int count);
}
