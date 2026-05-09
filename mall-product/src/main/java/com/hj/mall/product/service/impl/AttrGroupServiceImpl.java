package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.Attr;
import com.hj.mall.product.entity.AttrAttrgroupRelation;
import com.hj.mall.product.entity.AttrGroup;
import com.hj.mall.product.mapper.AttrAttrgroupRelationMapper;
import com.hj.mall.product.mapper.AttrGroupMapper;
import com.hj.mall.product.mapper.AttrMapper;
import com.hj.mall.product.service.AttrGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttrGroupServiceImpl implements AttrGroupService {

    private final AttrGroupMapper attrGroupMapper;
    private final AttrMapper attrMapper;
    private final AttrAttrgroupRelationMapper relationMapper;

    @Override
    public IPage<AttrGroup> listPage(Page<AttrGroup> page, Long categoryId, String key) {
        LambdaQueryWrapper<AttrGroup> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(AttrGroup::getCategoryId, categoryId);
        }
        if (StringUtils.hasText(key)) {
            wrapper.like(AttrGroup::getName, key);
        }
        wrapper.orderByAsc(AttrGroup::getSort);
        return attrGroupMapper.selectPage(page, wrapper);
    }

    @Override
    public AttrGroup getById(Long id) {
        return attrGroupMapper.selectById(id);
    }

    @Override
    public void save(AttrGroup group) {
        attrGroupMapper.insert(group);
    }

    @Override
    public void updateById(AttrGroup group) {
        attrGroupMapper.updateById(group);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        attrGroupMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Attr> getAttrRelation(Long attrGroupId) {
        List<AttrAttrgroupRelation> relations = relationMapper.selectList(
                new LambdaQueryWrapper<AttrAttrgroupRelation>()
                        .eq(AttrAttrgroupRelation::getAttrGroupId, attrGroupId)
        );
        if (relations.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> attrIds = relations.stream().map(AttrAttrgroupRelation::getAttrId).toList();
        return attrMapper.selectBatchIds(attrIds);
    }

    @Override
    public IPage<Attr> getNoAttrRelation(Long attrGroupId, Page<Attr> page, String key) {
        AttrGroup group = attrGroupMapper.selectById(attrGroupId);
        if (group == null) {
            return page;
        }

        List<AttrAttrgroupRelation> relations = relationMapper.selectList(
                new LambdaQueryWrapper<AttrAttrgroupRelation>()
                        .eq(AttrAttrgroupRelation::getAttrGroupId, attrGroupId)
        );
        List<Long> existAttrIds = relations.stream().map(AttrAttrgroupRelation::getAttrId).toList();

        LambdaQueryWrapper<Attr> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attr::getStatus, 1);
        if (!existAttrIds.isEmpty()) {
            wrapper.notIn(Attr::getId, existAttrIds);
        }
        if (StringUtils.hasText(key)) {
            wrapper.like(Attr::getName, key);
        }
        // 限定同分类下的属性
        wrapper.inSql(Attr::getId,
                "SELECT attr_id FROM pms_attr_attrgroup_relation WHERE attr_group_id IN " +
                        "(SELECT id FROM pms_attr_group WHERE category_id = " + group.getCategoryId() + ")");

        return attrMapper.selectPage(page, wrapper);
    }

    @Override
    public List<AttrGroup> listWithAttrs(Long categoryId) {
        List<AttrGroup> groups = attrGroupMapper.selectList(
                new LambdaQueryWrapper<AttrGroup>()
                        .eq(AttrGroup::getCategoryId, categoryId)
                        .orderByAsc(AttrGroup::getSort)
        );

        for (AttrGroup group : groups) {
            List<AttrAttrgroupRelation> relations = relationMapper.selectList(
                    new LambdaQueryWrapper<AttrAttrgroupRelation>()
                            .eq(AttrAttrgroupRelation::getAttrGroupId, group.getId())
            );
            if (!relations.isEmpty()) {
                List<Long> attrIds = relations.stream().map(AttrAttrgroupRelation::getAttrId).toList();
                group.setAttrs(attrMapper.selectBatchIds(attrIds));
            }
        }

        return groups;
    }
}
