package com.hj.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.Attr;

import java.util.List;

public interface AttrService {

    IPage<Attr> listPage(Page<Attr> page, String key);

    IPage<Attr> listByTypeAndCategory(Page<Attr> page, Integer attrType, Long categoryId);

    Attr getDetail(Long attrId);

    void save(Attr attr);

    void updateById(Attr attr);

    void removeBatch(List<Long> ids);
}
