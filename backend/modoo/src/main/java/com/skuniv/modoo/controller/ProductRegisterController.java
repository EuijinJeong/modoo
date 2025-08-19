package com.skuniv.modoo.controller;

import com.skuniv.modoo.entity.Product;
import com.skuniv.modoo.service.ProductService;
import com.skuniv.modoo.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductRegisterController {

    @Autowired
    private ProductService productService;

    @PostMapping("/productRegistration") // api 엔드포인트 지정
    public ResponseEntity<Product> saveProduct(@RequestPart("product") ProductDto productDto,
                                               @RequestPart("files") List<MultipartFile> files) {

        Product savedProduct = productService.saveProduct(productDto, files); // saveProduct 메소드 호출 결과를 변수에 할당
        return ResponseEntity.ok(savedProduct);
    }

}
