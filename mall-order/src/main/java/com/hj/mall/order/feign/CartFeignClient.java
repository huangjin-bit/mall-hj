package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.dto.CartItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 购物车远程调用客户端
 */
@FeignClient(
        name = "mall-cart",
        path = "/feign/cart",
        fallbackFactory = CartFeignFallback.class
)
public interface CartFeignClient {

    /**
     * 获取当前用户选中的购物车项（结算用）
     */
    @GetMapping("/checked/{memberId}")
    Result<List<CartItemDto>> getCheckedItems(@PathVariable("memberId") Long memberId);

    /**
     * 清除用户选中的购物车商品
     */
    @GetMapping("/clear/checked/{memberId}")
    Result<Void> clearChecked(@PathVariable("memberId") Long memberId);
}
