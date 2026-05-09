package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("pms_spu_info")
public class SpuInfo {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String spuName;

    private String spuDescription;

    private Long categoryId;

    private Long brandId;

    private BigDecimal weight;

    /** 0下架 1上架 */
    private Integer publishStatus;

    /** 0待审核 1审核通过 2审核拒绝 */
    private Integer auditStatus;

    /** 0否 1新品 */
    private Integer newStatus;

    /** 0否 1推荐 */
    private Integer recommendStatus;

    private Integer sort;

    private Long createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
