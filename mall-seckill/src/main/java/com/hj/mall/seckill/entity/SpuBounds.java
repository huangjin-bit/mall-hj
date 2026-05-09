package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SPU积分成长实体
 */
@Data
@TableName("sms_spu_bounds")
public class SpuBounds {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品SPU ID
     */
    private Long spuId;

    /**
     * 购买积分
     */
    private BigDecimal buyBounds;

    /**
     * 成长值
     */
    private BigDecimal growBounds;

    /**
     * 优惠生效情况：0->无优惠；1->有优惠
     */
    private Integer work;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
