package com.hj.mall.ware.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.ware.entity.WareOrderTaskDetail;
import com.hj.mall.ware.service.WareOrderTaskDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ware-order-task-detail")
@RequiredArgsConstructor
public class WareOrderTaskDetailController {

    private final WareOrderTaskDetailService wareOrderTaskDetailService;

    @GetMapping("/list")
    public Result<IPage<WareOrderTaskDetail>> list(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestParam(required = false) Long taskId) {
        Page<WareOrderTaskDetail> p = new Page<>(page, size);
        return Result.ok(wareOrderTaskDetailService.listPage(p, taskId));
    }

    @GetMapping("/{id}")
    public Result<WareOrderTaskDetail> getById(@PathVariable Long id) {
        return Result.ok(wareOrderTaskDetailService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody WareOrderTaskDetail wareOrderTaskDetail) {
        wareOrderTaskDetailService.save(wareOrderTaskDetail);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody WareOrderTaskDetail wareOrderTaskDetail) {
        wareOrderTaskDetailService.updateById(wareOrderTaskDetail);
        return Result.ok();
    }

    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        wareOrderTaskDetailService.removeBatch(ids);
        return Result.ok();
    }
}
