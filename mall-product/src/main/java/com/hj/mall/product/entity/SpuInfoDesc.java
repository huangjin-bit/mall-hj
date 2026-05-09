package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_spu_info_desc")
public class SpuInfoDesc {

    @TableId(type = IdType.ASSIGN_ID)
    private Long spuId;

    private String description;

    private LocalDateTime updateTime;
}
