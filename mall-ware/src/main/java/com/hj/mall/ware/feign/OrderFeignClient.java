package com.hj.mall.ware.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.ware.feign.fallback.OrderFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 订单远程调用客户端
 * 库存解锁时需要查询订单状态
 */
@FeignClient(value = "mall-order", fallback = OrderFeignFallback.class)
public interface OrderFeignClient {

    /**
     * 根据订单号查询订单状态
     * @param orderSn 订单号
     * @return 订单信息
     */
    @GetMapping("/order/status/{orderSn}")
    Result<Object> getOrderStatus(@PathVariable("orderSn") String orderSn);
}
