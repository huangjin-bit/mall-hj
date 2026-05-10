package com.hj.mall.ware.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.ware.entity.WareOrderTaskDetail;

import java.util.List;

public interface WareOrderTaskDetailService {

    List<WareOrderTaskDetail> listByTaskId(Long taskId);

    void save(WareOrderTaskDetail detail);

    void saveBatch(List<WareOrderTaskDetail> details);

    IPage<WareOrderTaskDetail> listPage(Page<WareOrderTaskDetail> page, Long taskId);

    WareOrderTaskDetail getById(Long id);

    void updateById(WareOrderTaskDetail detail);

    void removeBatch(List<Long> ids);
}
