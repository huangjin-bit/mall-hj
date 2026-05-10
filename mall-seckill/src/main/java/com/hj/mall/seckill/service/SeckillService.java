package com.hj.mall.seckill.service;

import com.hj.mall.seckill.vo.SeckillSkuVO;

import java.util.List;

/**
 * 秒杀服务接口
 */
public interface SeckillService {

    /**
     * 获取当前正在秒杀的商品列表
     * @return 秒杀商品列表
     */
    List<SeckillSkuVO> getCurrentSeckillSkus();

    /**
     * 查询某个SKU是否正在参与秒杀
     * @param skuId 商品SKU ID
     * @return 秒杀商品信息
     */
    SeckillSkuVO getSeckillSkuInfo(Long skuId);

    /**
     * 执行秒杀
     * @param killId 秒杀标识（sessionId_skuId）
     * @param code 随机码
     * @param num 购买数量
     * @param memberId 会员ID
     * @return 订单号
     */
    String kill(String killId, String code, Integer num, Long memberId);
}
