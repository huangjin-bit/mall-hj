package com.hj.mall.seckill.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.service.SeckillService;
import com.hj.mall.seckill.vo.SeckillSkuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 秒杀控制器
 */
@RestController
@RequestMapping("/seckill")
@RequiredArgsConstructor
public class SeckillController {

    private final SeckillService seckillService;

    /**
     * 获取当前正在秒杀的商品列表
     */
    @GetMapping("/current")
    public Result<List<SeckillSkuVO>> getCurrentSeckillSkus() {
        List<SeckillSkuVO> list = seckillService.getCurrentSeckillSkus();
        return Result.ok(list);
    }

    /**
     * 查询某个SKU是否正在参与秒杀
     */
    @GetMapping("/sku/{skuId}")
    public Result<SeckillSkuVO> getSeckillSkuInfo(@PathVariable("skuId") Long skuId) {
        SeckillSkuVO vo = seckillService.getSeckillSkuInfo(skuId);
        return Result.ok(vo);
    }

    /**
     * 执行秒杀
     */
    @GetMapping("/kill")
    public Result<String> seckill(
            @RequestParam("killId") String killId,
            @RequestParam("code") String code,
            @RequestParam(value = "num", defaultValue = "1") Integer num,
            @RequestParam("memberId") Long memberId) {
        String orderSn = seckillService.kill(killId, code, num, memberId);
        if (orderSn != null) {
            return Result.ok(orderSn);
        } else {
            return Result.error("秒杀失败");
        }
    }
}
