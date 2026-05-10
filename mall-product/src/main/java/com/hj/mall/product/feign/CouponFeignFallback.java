package com.hj.mall.product.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 秒杀服务Feign降级处理
 */
@Slf4j
@Component
public class CouponFeignFallback implements FallbackFactory<CouponFeignClient> {

    @Override
    public CouponFeignClient create(Throwable cause) {
        log.error("[CouponFeign] 调用失败: {}", cause.getMessage(), cause);
        return new CouponFeignClient() {
            @Override
            public Result<Void> saveSpuBounds(CouponFeignClient.SpuBoundsTO spuBoundsTO) {
                log.warn("[CouponFeign] 保存SPU积分边界失败，spuId: {}", spuBoundsTO != null ? spuBoundsTO.getSpuId() : null);
                return Result.error(ResultCode.INTERNAL_ERROR);
            }

            @Override
            public Result<Void> saveSkuReduction(CouponFeignClient.SkuReductionTO skuReductionTO) {
                log.warn("[CouponFeign] 保存SKU满减信息失败，skuId: {}", skuReductionTO != null ? skuReductionTO.getSkuId() : null);
                return Result.error(ResultCode.INTERNAL_ERROR);
            }

            @Override
            public Result<List<Object>> getActiveAdvs() {
                log.warn("[CouponFeign] 获取首页广告失败，返回空列表");
                return Result.ok(Collections.emptyList());
            }

            @Override
            public Result<List<Object>> getActiveSubjects() {
                log.warn("[CouponFeign] 获取首页专题失败，返回空列表");
                return Result.ok(Collections.emptyList());
            }
        };
    }
}
