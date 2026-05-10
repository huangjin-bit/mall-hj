package com.hj.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.ware.entity.WareOrderTask;
import com.hj.mall.ware.mapper.WareOrderTaskMapper;
import com.hj.mall.ware.service.WareOrderTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WareOrderTaskServiceImpl implements WareOrderTaskService {

    private final WareOrderTaskMapper wareOrderTaskMapper;

    @Override
    public WareOrderTask getByOrderSn(String orderSn) {
        return wareOrderTaskMapper.selectOne(
                new LambdaQueryWrapper<WareOrderTask>()
                        .eq(WareOrderTask::getOrderSn, orderSn)
        );
    }

    @Override
    public WareOrderTask getById(Long id) {
        return wareOrderTaskMapper.selectById(id);
    }

    @Override
    public void save(WareOrderTask task) {
        wareOrderTaskMapper.insert(task);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        WareOrderTask task = new WareOrderTask();
        task.setId(id);
        task.setTaskStatus(status);
        wareOrderTaskMapper.updateById(task);
    }

    @Override
    public IPage<WareOrderTask> listPage(Page<WareOrderTask> page) {
        return wareOrderTaskMapper.selectPage(page, null);
    }

    @Override
    public void updateById(WareOrderTask task) {
        wareOrderTaskMapper.updateById(task);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        wareOrderTaskMapper.deleteBatchIds(ids);
    }
}
