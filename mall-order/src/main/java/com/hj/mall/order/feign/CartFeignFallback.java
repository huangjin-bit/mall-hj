package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.dto.CartItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 购物车服务降级处理
 */
@Slf4j
@Component
public class CartFeignFallback implements FallbackFactory<CartFeignClient> {

    @Override
    public CartFeignClient create(Throwable cause) {
        return new CartFeignClient() {
            @Override
            public Result<List<CartItemDto>> getCheckedItems(Long memberId) {
                log.error("[CartFeignFallback] getCheckedItems 调用失败, memberId={}, error={}",
                        memberId, cause.getMessage());
                return Result.error("购物车服务暂不可用");
            }

            @Override
            public Result<Void> clearChecked(Long memberId) {
                log.error("[CartFeignFallback] clearChecked 调用失败, memberId={}, error={}",
                        memberId, cause.getMessage());
                return Result.error("购物车服务暂不可用");
            }
        };
    }
}
