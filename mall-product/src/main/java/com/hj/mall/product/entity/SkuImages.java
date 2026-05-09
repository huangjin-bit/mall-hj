package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_sku_images")
public class SkuImages {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long skuId;

    private String imgUrl;

    /** 0否 1默认图 */
    private Integer defaultImg;

    private Integer sort;

    private LocalDateTime createTime;
}
