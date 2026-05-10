package com.hj.mall.product.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页楼层配置VO
 */
@Data
public class HomeFloorConfigVO {

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 楼层名称
     */
    private String floorName;

    /**
     * 商品数量限制，默认6个
     */
    private Integer productLimit = 6;

    /**
     * 指定SKU ID列表
     */
    private List<Long> skuIds = new ArrayList<>();
}
