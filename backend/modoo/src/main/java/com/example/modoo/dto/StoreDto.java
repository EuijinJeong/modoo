package com.example.modoo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreDto {
    private Long id;
    private String email;
    private String name;
    private String note;
    private MemberResponseDto member;
    private List<ProductDto> products;
    private int productsCount;
}
