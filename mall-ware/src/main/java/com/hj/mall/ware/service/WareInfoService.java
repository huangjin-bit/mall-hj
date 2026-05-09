package com.hj.mall.ware.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.ware.entity.WareInfo;

import java.util.List;

public interface WareInfoService {

    WareInfo getById(Long id);

    IPage<WareInfo> listPage(Page<WareInfo> page, String key);

    void save(WareInfo wareInfo);

    void updateById(WareInfo wareInfo);

    void removeBatch(List<Long> ids);
}
