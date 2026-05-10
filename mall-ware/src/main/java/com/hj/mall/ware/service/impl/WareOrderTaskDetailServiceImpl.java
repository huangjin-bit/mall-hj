package com.hj.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.ware.entity.WareOrderTaskDetail;
import com.hj.mall.ware.mapper.WareOrderTaskDetailMapper;
import com.hj.mall.ware.service.WareOrderTaskDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WareOrderTaskDetailServiceImpl implements WareOrderTaskDetailService {

    private final WareOrderTaskDetailMapper wareOrderTaskDetailMapper;

    @Override
    public List<WareOrderTaskDetail> listByTaskId(Long taskId) {
        return wareOrderTaskDetailMapper.selectList(
                new LambdaQueryWrapper<WareOrderTaskDetail>()
                        .eq(WareOrderTaskDetail::getTaskId, taskId)
                        .orderByAsc(WareOrderTaskDetail::getCreateTime)
        );
    }

    @Override
    public void save(WareOrderTaskDetail detail) {
        wareOrderTaskDetailMapper.insert(detail);
    }

    @Override
    public void saveBatch(List<WareOrderTaskDetail> details) {
        if (details != null && !details.isEmpty()) {
            details.forEach(this::save);
        }
    }

    @Override
    public IPage<WareOrderTaskDetail> listPage(Page<WareOrderTaskDetail> page, Long taskId) {
        LambdaQueryWrapper<WareOrderTaskDetail> wrapper = new LambdaQueryWrapper<>();
        if (taskId != null) {
            wrapper.eq(WareOrderTaskDetail::getTaskId, taskId);
        }
        wrapper.orderByAsc(WareOrderTaskDetail::getCreateTime);
        return wareOrderTaskDetailMapper.selectPage(page, wrapper);
    }

    @Override
    public WareOrderTaskDetail getById(Long id) {
        return wareOrderTaskDetailMapper.selectById(id);
    }

    @Override
    public void updateById(WareOrderTaskDetail detail) {
        wareOrderTaskDetailMapper.updateById(detail);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        wareOrderTaskDetailMapper.deleteBatchIds(ids);
    }
}
