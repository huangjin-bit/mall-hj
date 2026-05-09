package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_attr_value")
public class AttrValue {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long attrId;

    private String value;

    private String icon;

    private Integer sort;

    private LocalDateTime createTime;
}
