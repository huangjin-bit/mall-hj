package com.hj.mall.ware.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.ware.entity.WareOrderTask;
import com.hj.mall.ware.service.WareOrderTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ware-order-task")
@RequiredArgsConstructor
public class WareOrderTaskController {

    private final WareOrderTaskService wareOrderTaskService;

    @GetMapping("/list")
    public Result<IPage<WareOrderTask>> list(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size) {
        Page<WareOrderTask> p = new Page<>(page, size);
        return Result.ok(wareOrderTaskService.listPage(p));
    }

    @GetMapping("/{id}")
    public Result<WareOrderTask> getById(@PathVariable Long id) {
        return Result.ok(wareOrderTaskService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody WareOrderTask wareOrderTask) {
        wareOrderTaskService.save(wareOrderTask);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody WareOrderTask wareOrderTask) {
        wareOrderTaskService.updateById(wareOrderTask);
        return Result.ok();
    }

    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        wareOrderTaskService.removeBatch(ids);
        return Result.ok();
    }
}
