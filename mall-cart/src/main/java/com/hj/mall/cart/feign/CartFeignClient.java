package com.hj.mall.cart.feign;

import com.hj.mall.cart.model.CartItem;
import com.hj.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 购物车 Feign 客户端
 * 供其他服务（如订单服务）调用
 */
@FeignClient(
        name = "mall-cart",
        path = "/feign",
        fallbackFactory = CartFeignFallback.class
)
public interface CartFeignClient {

    /**
     * 获取用户选中的购物车商品
     *
     * @param memberId 用户ID
     * @return 选中的商品列表
     */
    @GetMapping("/cart/checked/{memberId}")
    Result<List<CartItem>> getCheckedItems(@PathVariable("memberId") Long memberId);

    /**
     * 清除用户选中的购物车商品
     * 用于订单创建成功后清除购物车
     *
     * @param memberId 用户ID
     * @return 操作结果
     */
    @PostMapping("/cart/clear/checked/{memberId}")
    Result<Void> clearChecked(@PathVariable("memberId") Long memberId);
}
