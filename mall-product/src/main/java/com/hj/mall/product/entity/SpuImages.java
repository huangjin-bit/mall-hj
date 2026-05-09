package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_spu_images")
public class SpuImages {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long spuId;

    private String imgUrl;

    private Integer sort;

    private LocalDateTime createTime;
}
