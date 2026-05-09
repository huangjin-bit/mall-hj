package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_spu_attr_value")
public class SpuAttrValue {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long spuId;

    private Long attrId;

    private String attrName;

    private String attrValue;

    private Integer sort;

    private LocalDateTime createTime;
}
