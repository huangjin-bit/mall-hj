package com.hj.mall.ware.service;

import com.hj.mall.ware.entity.WareOrderTaskDetail;

import java.util.List;

public interface WareOrderTaskDetailService {

    List<WareOrderTaskDetail> listByTaskId(Long taskId);

    void save(WareOrderTaskDetail detail);

    void saveBatch(List<WareOrderTaskDetail> details);
}
