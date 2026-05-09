package com.hj.mall.cart.feign;

import com.hj.mall.cart.model.CartItem;
import com.hj.mall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 购物车 Feign 降级工厂
 * 当调用失败时返回默认值
 */
@Slf4j
@Component
public class CartFeignFallback implements FallbackFactory<CartFeignClient> {

    @Override
    public CartFeignClient create(Throwable cause) {
        log.error("[CartFeignFallback] Feign call failed", cause);

        return new CartFeignClient() {

            @Override
            public Result<List<CartItem>> getCheckedItems(Long memberId) {
                log.error("[CartFeignFallback] Get checked items failed: memberId={}", memberId);
                return Result.success(Collections.emptyList());
            }

            @Override
            public Result<Void> clearChecked(Long memberId) {
                log.error("[CartFeignFallback] Clear checked failed: memberId={}", memberId);
                return Result.success();
            }
        };
    }
}
