package com.hj.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
}
