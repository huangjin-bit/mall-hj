package com.hj.mall.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.mall.seckill.entity.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀订单Mapper
 */
@Mapper
public interface SeckillOrderMapper extends BaseMapper<SeckillOrder> {
}
