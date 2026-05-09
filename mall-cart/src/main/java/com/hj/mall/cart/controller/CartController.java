package com.hj.mall.cart.controller;

import com.hj.mall.cart.model.CartItem;
import com.hj.mall.cart.service.CartService;
import com.hj.mall.cart.vo.CartVO;
import com.hj.mall.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 */
@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 获取购物车
     *
     * @param memberId 用户ID（从网关传递的请求头中获取）
     * @return 购物车信息
     */
    @GetMapping("/list")
    public Result<CartVO> getCart(@RequestHeader("X-User-Id") Long memberId) {
        log.debug("[CartController] Get cart: memberId={}", memberId);
        CartVO cartVO = cartService.getCart(memberId);
        return Result.success(cartVO);
    }

    /**
     * 添加商品到购物车
     *
     * @param memberId 用户ID
     * @param skuId    SKU ID
     * @param quantity 数量
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<Void> addToCart(
            @RequestHeader("X-User-Id") Long memberId,
            @RequestParam Long skuId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        log.debug("[CartController] Add to cart: memberId={}, skuId={}, quantity={}",
                memberId, skuId, quantity);
        cartService.addToCart(memberId, skuId, quantity);
        return Result.success();
    }

    /**
     * 更新购物车商品数量
     *
     * @param memberId 用户ID
     * @param skuId    SKU ID
     * @param quantity 新数量
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result<Void> updateQuantity(
            @RequestHeader("X-User-Id") Long memberId,
            @RequestParam Long skuId,
            @RequestParam Integer quantity) {
        log.debug("[CartController] Update quantity: memberId={}, skuId={}, quantity={}",
                memberId, skuId, quantity);
        cartService.updateQuantity(memberId, skuId, quantity);
        return Result.success();
    }

    /**
     * 选中/取消选中商品
     *
     * @param memberId 用户ID
     * @param skuId    SKU ID
     * @param checked  是否选中
     * @return 操作结果
     */
    @PostMapping("/check")
    public Result<Void> checkItem(
            @RequestHeader("X-User-Id") Long memberId,
            @RequestParam Long skuId,
            @RequestParam Boolean checked) {
        log.debug("[CartController] Check item: memberId={}, skuId={}, checked={}",
                memberId, skuId, checked);
        cartService.checkItem(memberId, skuId, checked);
        return Result.success();
    }

    /**
     * 全选/取消全选
     *
     * @param memberId 用户ID
     * @param checked  是否选中
     * @return 操作结果
     */
    @PostMapping("/check/all")
    public Result<Void> checkAll(
            @RequestHeader("X-User-Id") Long memberId,
            @RequestParam Boolean checked) {
        log.debug("[CartController] Check all: memberId={}, checked={}", memberId, checked);
        cartService.checkAll(memberId, checked);
        return Result.success();
    }

    /**
     * 删除购物车商品
     *
     * @param memberId 用户ID
     * @param skuIds   SKU ID 列表
     * @return 操作结果
     */
    @PostMapping("/delete")
    public Result<Void> deleteItems(
            @RequestHeader("X-User-Id") Long memberId,
            @RequestBody List<Long> skuIds) {
        log.debug("[CartController] Delete items: memberId={}, count={}", memberId, skuIds.size());
        cartService.deleteItems(memberId, skuIds);
        return Result.success();
    }
}
