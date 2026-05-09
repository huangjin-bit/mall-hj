package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_category_brand_relation")
public class CategoryBrandRelation {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long categoryId;

    private Long brandId;

    private Integer sort;

    private LocalDateTime createTime;
}
