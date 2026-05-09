package com.hj.mall.cart.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hj.mall.cart.model.CartItem;
import com.hj.mall.cart.service.CartService;
import com.hj.mall.cart.vo.CartVO;
import com.hj.mall.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 购物车服务实现
 * 使用 Redis Hash 存储购物车数据
 * Key: cart:{memberId}
 * Value: Hash<skuId, CartItem_JSON>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final String CART_KEY_PREFIX = "cart:";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public CartVO getCart(Long memberId) {
        String cartKey = CART_KEY_PREFIX + memberId;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(cartKey);

        List<CartItem> items = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            try {
                CartItem item = OBJECT_MAPPER.readValue((String) entry.getValue(), CartItem.class);
                items.add(item);
            } catch (Exception e) {
                log.error("[CartService] Failed to parse cart item: memberId={}, skuId={}, error={}",
                        memberId, entry.getKey(), e.getMessage());
            }
        }

        CartVO cartVO = new CartVO();
        cartVO.setItems(items);
        calculateTotals(cartVO);

        log.debug("[CartService] Get cart: memberId={}, itemCount={}", memberId, items.size());
        return cartVO;
    }

    @Override
    public void addToCart(Long memberId, Long skuId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BizException("数量必须大于0");
        }

        String cartKey = CART_KEY_PREFIX + memberId;
        String skuIdStr = String.valueOf(skuId);

        // 检查商品是否已在购物车中
        String existingJson = (String) stringRedisTemplate.opsForHash().get(cartKey, skuIdStr);

        if (existingJson != null) {
            // 商品已存在，增加数量
            try {
                CartItem existingItem = OBJECT_MAPPER.readValue(existingJson, CartItem.class);
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                String updatedJson = OBJECT_MAPPER.writeValueAsString(existingItem);
                stringRedisTemplate.opsForHash().put(cartKey, skuIdStr, updatedJson);
                log.debug("[CartService] Update cart item quantity: memberId={}, skuId={}, newQuantity={}",
                        memberId, skuId, existingItem.getQuantity());
            } catch (Exception e) {
                log.error("[CartService] Failed to update cart item: memberId={}, skuId={}, error={}",
                        memberId, skuId, e.getMessage());
                throw new BizException("更新购物车失败");
            }
        } else {
            // 新增商品（实际项目中需要从商品服务获取商品信息）
            CartItem newItem = new CartItem();
            newItem.setSkuId(skuId);
            newItem.setQuantity(quantity);
            newItem.setIsChecked(true);

            try {
                String json = OBJECT_MAPPER.writeValueAsString(newItem);
                stringRedisTemplate.opsForHash().put(cartKey, skuIdStr, json);
                log.debug("[CartService] Add new cart item: memberId={}, skuId={}, quantity={}",
                        memberId, skuId, quantity);
            } catch (Exception e) {
                log.error("[CartService] Failed to add cart item: memberId={}, skuId={}, error={}",
                        memberId, skuId, e.getMessage());
                throw new BizException("添加到购物车失败");
            }
        }
    }

    @Override
    public void updateQuantity(Long memberId, Long skuId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BizException("数量必须大于0");
        }

        String cartKey = CART_KEY_PREFIX + memberId;
        String skuIdStr = String.valueOf(skuId);

        String existingJson = (String) stringRedisTemplate.opsForHash().get(cartKey, skuIdStr);
        if (existingJson == null) {
            throw new BizException("商品不存在于购物车中");
        }

        try {
            CartItem item = OBJECT_MAPPER.readValue(existingJson, CartItem.class);
            item.setQuantity(quantity);
            String updatedJson = OBJECT_MAPPER.writeValueAsString(item);
            stringRedisTemplate.opsForHash().put(cartKey, skuIdStr, updatedJson);
            log.debug("[CartService] Update quantity: memberId={}, skuId={}, quantity={}",
                    memberId, skuId, quantity);
        } catch (Exception e) {
            log.error("[CartService] Failed to update quantity: memberId={}, skuId={}, error={}",
                    memberId, skuId, e.getMessage());
            throw new BizException("更新数量失败");
        }
    }

    @Override
    public void checkItem(Long memberId, Long skuId, Boolean checked) {
        String cartKey = CART_KEY_PREFIX + memberId;
        String skuIdStr = String.valueOf(skuId);

        String existingJson = (String) stringRedisTemplate.opsForHash().get(cartKey, skuIdStr);
        if (existingJson == null) {
            throw new BizException("商品不存在于购物车中");
        }

        try {
            CartItem item = OBJECT_MAPPER.readValue(existingJson, CartItem.class);
            item.setIsChecked(checked);
            String updatedJson = OBJECT_MAPPER.writeValueAsString(item);
            stringRedisTemplate.opsForHash().put(cartKey, skuIdStr, updatedJson);
            log.debug("[CartService] Check item: memberId={}, skuId={}, checked={}",
                    memberId, skuId, checked);
        } catch (Exception e) {
            log.error("[CartService] Failed to check item: memberId={}, skuId={}, error={}",
                    memberId, skuId, e.getMessage());
            throw new BizException("更新选中状态失败");
        }
    }

    @Override
    public void checkAll(Long memberId, Boolean checked) {
        String cartKey = CART_KEY_PREFIX + memberId;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(cartKey);

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            try {
                CartItem item = OBJECT_MAPPER.readValue((String) entry.getValue(), CartItem.class);
                item.setIsChecked(checked);
                String json = OBJECT_MAPPER.writeValueAsString(item);
                stringRedisTemplate.opsForHash().put(cartKey, String.valueOf(entry.getKey()), json);
            } catch (Exception e) {
                log.error("[CartService] Failed to check item: memberId={}, skuId={}, error={}",
                        memberId, entry.getKey(), e.getMessage());
            }
        }

        log.debug("[CartService] Check all: memberId={}, checked={}, count={}",
                memberId, checked, entries.size());
    }

    @Override
    public void deleteItem(Long memberId, Long skuId) {
        String cartKey = CART_KEY_PREFIX + memberId;
        String skuIdStr = String.valueOf(skuId);

        Long deleted = stringRedisTemplate.opsForHash().delete(cartKey, skuIdStr);
        if (deleted == 0) {
            throw new BizException("商品不存在于购物车中");
        }

        log.debug("[CartService] Delete item: memberId={}, skuId={}", memberId, skuId);
    }

    @Override
    public void deleteItems(Long memberId, List<Long> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            throw new BizException("SKU ID 列表不能为空");
        }

        String cartKey = CART_KEY_PREFIX + memberId;
        String[] skuIdStrs = skuIds.stream()
                .map(String::valueOf)
                .toArray(String[]::new);

        stringRedisTemplate.opsForHash().delete(cartKey, skuIdStrs);

        log.debug("[CartService] Delete items: memberId={}, count={}", memberId, skuIds.size());
    }

    @Override
    public List<CartItem> getCheckedItems(Long memberId) {
        String cartKey = CART_KEY_PREFIX + memberId;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(cartKey);

        List<CartItem> checkedItems = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            try {
                CartItem item = OBJECT_MAPPER.readValue((String) entry.getValue(), CartItem.class);
                if (Boolean.TRUE.equals(item.getIsChecked())) {
                    checkedItems.add(item);
                }
            } catch (Exception e) {
                log.error("[CartService] Failed to parse cart item: memberId={}, skuId={}, error={}",
                        memberId, entry.getKey(), e.getMessage());
            }
        }

        log.debug("[CartService] Get checked items: memberId={}, count={}",
                memberId, checkedItems.size());
        return checkedItems;
    }

    @Override
    public void clearChecked(Long memberId) {
        String cartKey = CART_KEY_PREFIX + memberId;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(cartKey);

        List<String> skuIdsToDelete = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            try {
                CartItem item = OBJECT_MAPPER.readValue((String) entry.getValue(), CartItem.class);
                if (Boolean.TRUE.equals(item.getIsChecked())) {
                    skuIdsToDelete.add(String.valueOf(entry.getKey()));
                }
            } catch (Exception e) {
                log.error("[CartService] Failed to parse cart item: memberId={}, skuId={}, error={}",
                        memberId, entry.getKey(), e.getMessage());
            }
        }

        if (!skuIdsToDelete.isEmpty()) {
            stringRedisTemplate.opsForHash().delete(cartKey, skuIdsToDelete.toArray(new String[0]));
            log.debug("[CartService] Clear checked items: memberId={}, count={}",
                    memberId, skuIdsToDelete.size());
        }
    }

    /**
     * 计算购物车总计信息
     */
    private void calculateTotals(CartVO cartVO) {
        if (cartVO.getItems() == null || cartVO.getItems().isEmpty()) {
            cartVO.setTotalCount(0);
            cartVO.setCheckedCount(0);
            cartVO.setTotalPrice(BigDecimal.ZERO);
            cartVO.setTotalOriginalPrice(BigDecimal.ZERO);
            return;
        }

        int totalCount = 0;
        int checkedCount = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalOriginalPrice = BigDecimal.ZERO;

        for (CartItem item : cartVO.getItems()) {
            totalCount += item.getQuantity();

            if (Boolean.TRUE.equals(item.getIsChecked())) {
                checkedCount += item.getQuantity();

                BigDecimal price = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
                BigDecimal originalPrice = item.getOriginalPrice() != null ?
                        item.getOriginalPrice() : BigDecimal.ZERO;

                totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
                totalOriginalPrice = totalOriginalPrice.add(
                        originalPrice.multiply(BigDecimal.valueOf(item.getQuantity()))
                );
            }
        }

        cartVO.setTotalCount(totalCount);
        cartVO.setCheckedCount(checkedCount);
        cartVO.setTotalPrice(totalPrice);
        cartVO.setTotalOriginalPrice(totalOriginalPrice);
    }
}
