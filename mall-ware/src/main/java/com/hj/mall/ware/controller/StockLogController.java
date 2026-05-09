package com.hj.mall.ware.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.ware.entity.StockLog;
import com.hj.mall.ware.service.StockLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-log")
@RequiredArgsConstructor
public class StockLogController {

    private final StockLogService stockLogService;

    @GetMapping("/list")
    public Result<IPage<StockLog>> list(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) Long wareId,
                                        @RequestParam(required = false) Long skuId) {
        Page<StockLog> p = new Page<>(page, size);
        return Result.ok(stockLogService.listPage(p, wareId, skuId));
    }
}
