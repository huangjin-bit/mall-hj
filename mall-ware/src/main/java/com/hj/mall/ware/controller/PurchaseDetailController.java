package com.hj.mall.ware.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.ware.entity.PurchaseDetail;
import com.hj.mall.ware.service.PurchaseDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase-detail")
@RequiredArgsConstructor
public class PurchaseDetailController {

    private final PurchaseDetailService purchaseDetailService;

    @GetMapping("/list/{purchaseId}")
    public Result<List<PurchaseDetail>> listByPurchaseId(@PathVariable Long purchaseId) {
        return Result.ok(purchaseDetailService.listByPurchaseId(purchaseId));
    }

    @PostMapping
    public Result<Void> save(@RequestBody PurchaseDetail detail) {
        purchaseDetailService.saveBatch(List.of(detail));
        return Result.ok();
    }

    @PostMapping("/batch")
    public Result<Void> saveBatch(@RequestBody List<PurchaseDetail> details) {
        purchaseDetailService.saveBatch(details);
        return Result.ok();
    }
}
