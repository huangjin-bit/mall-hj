package com.hj.mall.product.service;

import com.hj.mall.product.entity.AttrAttrgroupRelation;

import java.util.List;

public interface AttrAttrgroupRelationService {

    void saveBatch(List<AttrAttrgroupRelation> relations);

    void removeBatch(List<AttrAttrgroupRelation> relations);
}
