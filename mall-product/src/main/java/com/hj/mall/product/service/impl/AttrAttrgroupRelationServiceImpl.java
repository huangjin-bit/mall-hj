package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.product.entity.AttrAttrgroupRelation;
import com.hj.mall.product.mapper.AttrAttrgroupRelationMapper;
import com.hj.mall.product.service.AttrAttrgroupRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttrAttrgroupRelationServiceImpl implements AttrAttrgroupRelationService {

    private final AttrAttrgroupRelationMapper relationMapper;

    @Override
    public void saveBatch(List<AttrAttrgroupRelation> relations) {
        for (AttrAttrgroupRelation relation : relations) {
            relationMapper.insert(relation);
        }
    }

    @Override
    public void removeBatch(List<AttrAttrgroupRelation> relations) {
        for (AttrAttrgroupRelation relation : relations) {
            relationMapper.delete(new LambdaQueryWrapper<AttrAttrgroupRelation>()
                    .eq(AttrAttrgroupRelation::getAttrId, relation.getAttrId())
                    .eq(AttrAttrgroupRelation::getAttrGroupId, relation.getAttrGroupId()));
        }
    }
}
