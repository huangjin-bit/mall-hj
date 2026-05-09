package com.hj.mall.search.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SearchParam {
    private String keyword;
    private Long categoryId;
    private Long brandId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer page = 1;
    private Integer size = 20;
    private String sort;
}
