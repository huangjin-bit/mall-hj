package com.hj.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.ware.entity.StockLog;
import com.hj.mall.ware.entity.WareOrderTask;
import com.hj.mall.ware.entity.WareOrderTaskDetail;
import com.hj.mall.ware.entity.WareSku;
import com.hj.mall.ware.mapper.WareSkuMapper;
import com.hj.mall.ware.service.StockLogService;
import com.hj.mall.ware.service.WareOrderTaskDetailService;
import com.hj.mall.ware.service.WareOrderTaskService;
import com.hj.mall.ware.service.WareSkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WareSkuServiceImpl implements WareSkuService {

    private final WareSkuMapper wareSkuMapper;
    private final WareOrderTaskService wareOrderTaskService;
    private final WareOrderTaskDetailService wareOrderTaskDetailService;
    private final StockLogService stockLogService;

    @Override
    public IPage<WareSku> listPage(Page<WareSku> page, Long wareId, Long skuId) {
        LambdaQueryWrapper<WareSku> wrapper = new LambdaQueryWrapper<>();
        if (wareId != null) {
            wrapper.eq(WareSku::getWareId, wareId);
        }
        if (skuId != null) {
            wrapper.eq(WareSku::getSkuId, skuId);
        }
        wrapper.orderByDesc(WareSku::getCreateTime);
        return wareSkuMapper.selectPage(page, wrapper);
    }

    @Override
    public WareSku getById(Long id) {
        WareSku wareSku = wareSkuMapper.selectById(id);
        if (wareSku == null) {
            throw new BizException(ResultCode.WARE_NOT_FOUND);
        }
        return wareSku;
    }

    @Override
    public void save(WareSku wareSku) {
        wareSkuMapper.insert(wareSku);
    }

    @Override
    public void updateById(WareSku wareSku) {
        wareSkuMapper.updateById(wareSku);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        wareSkuMapper.deleteBatchIds(ids);
    }

    @Override
    public Map<Long, Integer> getStockBySkuIds(List<Long> skuIds) {
        Map<Long, Integer> stockMap = new HashMap<>();
        if (skuIds == null || skuIds.isEmpty()) {
            return stockMap;
        }

        List<WareSku> wareSkuList = wareSkuMapper.selectList(
                new LambdaQueryWrapper<WareSku>()
                        .in(WareSku::getSkuId, skuIds)
        );

        for (WareSku wareSku : wareSkuList) {
            stockMap.merge(wareSku.getSkuId(), wareSku.getStock(), Integer::sum);
        }
        return stockMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockStock(Long orderId, String orderSn, List<Map<String, Object>> items) {
        log.info("[lockStock] 开始锁定库存，订单号：{}, 商品数：{}", orderSn, items.size());

        // 创建库存任务
        WareOrderTask task = new WareOrderTask();
        task.setOrderSn(orderSn);
        task.setOrderId(orderId);
        task.setTaskStatus(0); // 0-待处理
        wareOrderTaskService.save(task);

        for (Map<String, Object> item : items) {
            Long skuId = Long.valueOf(item.get("skuId").toString());
            Integer quantity = Integer.valueOf(item.get("quantity").toString());
            String skuName = item.get("skuName") != null ? item.get("skuName").toString() : "";

            // 查找有库存的仓库
            List<WareSku> wareSkuList = wareSkuMapper.selectList(
                    new LambdaQueryWrapper<WareSku>()
                            .eq(WareSku::getSkuId, skuId)
                            .gt(WareSku::getStock, quantity)
            );

            if (wareSkuList.isEmpty()) {
                log.warn("[lockStock] SKU {} 库存不足，需要：{}", skuId, quantity);
                throw new BizException(ResultCode.WARE_STOCK_NOT_ENOUGH);
            }

            // 选择第一个有库存的仓库
            WareSku wareSku = wareSkuList.get(0);
            Integer beforeStock = wareSku.getStock();
            Integer beforeLocked = wareSku.getStockLocked();

            // 锁定库存：增加锁定库存，扣减可用库存
            wareSku.setStockLocked(wareSku.getStockLocked() + quantity);
            wareSku.setStock(wareSku.getStock() - quantity);
            int updated = wareSkuMapper.updateById(wareSku);
            if (updated == 0) {
                log.error("[lockStock] 锁定库存失败，SKU：{}", skuId);
                throw new BizException(ResultCode.WARE_STOCK_NOT_ENOUGH);
            }

            // 记录库存日志
            StockLog stockLog = new StockLog();
            stockLog.setWareId(wareSku.getWareId());
            stockLog.setSkuId(skuId);
            stockLog.setChangeType("LOCK");
            stockLog.setChangeAmount(quantity);
            stockLog.setBeforeStock(beforeStock);
            stockLog.setAfterStock(wareSku.getStock());
            stockLog.setBeforeLocked(beforeLocked);
            stockLog.setAfterLocked(wareSku.getStockLocked());
            stockLog.setOrderSn(orderSn);
            stockLogService.save(stockLog);

            // 创建任务详情
            WareOrderTaskDetail detail = new WareOrderTaskDetail();
            detail.setTaskId(task.getId());
            detail.setWareId(wareSku.getWareId());
            detail.setSkuId(skuId);
            detail.setSkuName(skuName);
            detail.setLockQuantity(quantity);
            detail.setTaskDetailStatus(0); // 0-已锁定
            wareOrderTaskDetailService.save(detail);
        }

        log.info("[lockStock] 库存锁定成功，任务ID：{}", task.getId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductStock(Long taskId) {
        log.info("[deductStock] 开始扣减库存，任务ID：{}", taskId);

        WareOrderTask task = wareOrderTaskService.getById(taskId);
        if (task == null) {
            log.warn("[deductStock] 库存任务不存在，任务ID：{}", taskId);
            return;
        }

        List<WareOrderTaskDetail> details = wareOrderTaskDetailService.listByTaskId(taskId);
        for (WareOrderTaskDetail detail : details) {
            WareSku wareSku = wareSkuMapper.selectOne(
                    new LambdaQueryWrapper<WareSku>()
                            .eq(WareSku::getWareId, detail.getWareId())
                            .eq(WareSku::getSkuId, detail.getSkuId())
            );

            if (wareSku != null) {
                Integer beforeStock = wareSku.getStock();
                Integer beforeLocked = wareSku.getStockLocked();

                // 扣减库存
                wareSku.setStockLocked(wareSku.getStockLocked() - detail.getLockQuantity());
                wareSkuMapper.updateById(wareSku);

                // 记录库存日志
                StockLog stockLog = new StockLog();
                stockLog.setWareId(detail.getWareId());
                stockLog.setSkuId(detail.getSkuId());
                stockLog.setChangeType("DEDUCT");
                stockLog.setChangeAmount(detail.getLockQuantity());
                stockLog.setBeforeStock(beforeStock);
                stockLog.setAfterStock(wareSku.getStock());
                stockLog.setBeforeLocked(beforeLocked);
                stockLog.setAfterLocked(wareSku.getStockLocked());
                stockLog.setOrderSn(task.getOrderSn());
                stockLogService.save(stockLog);
            }
        }

        // 更新任务状态
        wareOrderTaskService.updateStatus(taskId, 1); // 1-已完成
        log.info("[deductStock] 库存扣减完成，任务ID：{}", taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseStock(Long taskId) {
        log.info("[releaseStock] 开始释放库存，任务ID：{}", taskId);

        WareOrderTask task = wareOrderTaskService.getById(taskId);
        if (task == null) {
            log.warn("[releaseStock] 库存任务不存在，任务ID：{}", taskId);
            return;
        }

        List<WareOrderTaskDetail> details = wareOrderTaskDetailService.listByTaskId(taskId);
        for (WareOrderTaskDetail detail : details) {
            WareSku wareSku = wareSkuMapper.selectOne(
                    new LambdaQueryWrapper<WareSku>()
                            .eq(WareSku::getWareId, detail.getWareId())
                            .eq(WareSku::getSkuId, detail.getSkuId())
            );

            if (wareSku != null) {
                Integer beforeStock = wareSku.getStock();
                Integer beforeLocked = wareSku.getStockLocked();

                // 释放锁定库存
                wareSku.setStockLocked(wareSku.getStockLocked() - detail.getLockQuantity());
                wareSku.setStock(wareSku.getStock() + detail.getLockQuantity());
                wareSkuMapper.updateById(wareSku);

                // 记录库存日志
                StockLog stockLog = new StockLog();
                stockLog.setWareId(detail.getWareId());
                stockLog.setSkuId(detail.getSkuId());
                stockLog.setChangeType("RELEASE");
                stockLog.setChangeAmount(detail.getLockQuantity());
                stockLog.setBeforeStock(beforeStock);
                stockLog.setAfterStock(wareSku.getStock());
                stockLog.setBeforeLocked(beforeLocked);
                stockLog.setAfterLocked(wareSku.getStockLocked());
                stockLog.setOrderSn(task.getOrderSn());
                stockLogService.save(stockLog);
            }
        }

        // 更新任务状态
        wareOrderTaskService.updateStatus(taskId, 2); // 2-已释放
        log.info("[releaseStock] 库存释放完成，任务ID：{}", taskId);
    }
}
