package com.hj.mall.cart.feign;

import com.hj.mall.cart.model.CartItem;
import com.hj.mall.cart.service.CartService;
import com.hj.mall.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车 Feign 控制器
 * 提供给其他服务调用的内部接口
 */
@Slf4j
@RestController
@RequestMapping("/feign/cart")
@RequiredArgsConstructor
public class CartFeignController {

    private final CartService cartService;

    /**
     * 获取用户选中的购物车商品
     *
     * @param memberId 用户ID
     * @return 选中的商品列表
     */
    @GetMapping("/checked/{memberId}")
    public Result<List<CartItem>> getCheckedItems(@PathVariable Long memberId) {
        log.debug("[CartFeignController] Get checked items: memberId={}", memberId);
        List<CartItem> items = cartService.getCheckedItems(memberId);
        return Result.success(items);
    }

    /**
     * 清除用户选中的购物车商品
     *
     * @param memberId 用户ID
     * @return 操作结果
     */
    @PostMapping("/clear/checked/{memberId}")
    public Result<Void> clearChecked(@PathVariable Long memberId) {
        log.debug("[CartFeignController] Clear checked: memberId={}", memberId);
        cartService.clearChecked(memberId);
        return Result.success();
    }
}
