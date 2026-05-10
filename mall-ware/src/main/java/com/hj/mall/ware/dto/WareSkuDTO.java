package com.hj.mall.ware.dto;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商品库存 - 请求DTO（替代直接暴露WareSkuEntity）
 */
@Data
public class WareSkuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "skuId不能为空")
    private Long skuId;

    @NotNull(message = "仓库id不能为空")
    private Long wareId;

    @Min(value = 0, message = "库存数不能为负")
    private Integer stock;

    private String skuName;

    private Integer stockLocked;
}
