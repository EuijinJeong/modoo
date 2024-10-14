package com.example.modoo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String title;
    private String condition;
    private String description;
    private Double price;
    private Boolean acceptOffers;
    private List<String> fileUrls;
    private StoreDto store;
    private Long likedCount;
}
