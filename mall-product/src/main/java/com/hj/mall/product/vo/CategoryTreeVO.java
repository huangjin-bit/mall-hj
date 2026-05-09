package com.hj.mall.product.vo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeVO {

    private Long id;

    private String name;

    private Long parentId;

    private Integer level;

    private String icon;

    private Integer sort;

    private List<CategoryTreeVO> children;
}
