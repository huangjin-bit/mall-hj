package com.hj.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.Attr;
import com.hj.mall.product.entity.AttrGroup;

import java.util.List;

public interface AttrGroupService {

    IPage<AttrGroup> listPage(Page<AttrGroup> page, Long categoryId, String key);

    AttrGroup getById(Long id);

    void save(AttrGroup group);

    void updateById(AttrGroup group);

    void removeBatch(List<Long> ids);

    List<Attr> getAttrRelation(Long attrGroupId);

    IPage<Attr> getNoAttrRelation(Long attrGroupId, Page<Attr> page, String key);

    List<AttrGroup> listWithAttrs(Long categoryId);
}
