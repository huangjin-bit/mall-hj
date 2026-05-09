package com.hj.mall.ware.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.ware.entity.WareInfo;
import com.hj.mall.ware.service.WareInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ware-info")
@RequiredArgsConstructor
public class WareInfoController {

    private final WareInfoService wareInfoService;

    @GetMapping("/list")
    public Result<IPage<WareInfo>> list(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) String key) {
        Page<WareInfo> p = new Page<>(page, size);
        return Result.ok(wareInfoService.listPage(p, key));
    }

    @GetMapping("/{id}")
    public Result<WareInfo> getById(@PathVariable Long id) {
        return Result.ok(wareInfoService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody WareInfo wareInfo) {
        wareInfoService.save(wareInfo);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody WareInfo wareInfo) {
        wareInfoService.updateById(wareInfo);
        return Result.ok();
    }

    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        wareInfoService.removeBatch(ids);
        return Result.ok();
    }
}
