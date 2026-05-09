package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.product.entity.Attr;
import com.hj.mall.product.entity.AttrAttrgroupRelation;
import com.hj.mall.product.entity.AttrValue;
import com.hj.mall.product.mapper.AttrAttrgroupRelationMapper;
import com.hj.mall.product.mapper.AttrMapper;
import com.hj.mall.product.mapper.AttrValueMapper;
import com.hj.mall.product.service.AttrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttrServiceImpl implements AttrService {

    private final AttrMapper attrMapper;
    private final AttrValueMapper attrValueMapper;
    private final AttrAttrgroupRelationMapper relationMapper;

    @Override
    public IPage<Attr> listPage(Page<Attr> page, String key) {
        LambdaQueryWrapper<Attr> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(key)) {
            wrapper.like(Attr::getName, key);
        }
        wrapper.orderByDesc(Attr::getCreateTime);
        return attrMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<Attr> listByTypeAndCategory(Page<Attr> page, Integer attrType, Long categoryId) {
        LambdaQueryWrapper<Attr> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attr::getAttrType, attrType);
        wrapper.eq(Attr::getStatus, 1);
        wrapper.orderByAsc(Attr::getSort);

        if (categoryId != null) {
            List<Long> attrIds = relationMapper.selectList(
                    new LambdaQueryWrapper<AttrAttrgroupRelation>()
                            .inSql(AttrAttrgroupRelation::getAttrGroupId,
                                    "SELECT id FROM pms_attr_group WHERE category_id = " + categoryId)
            ).stream().map(AttrAttrgroupRelation::getAttrId).toList();

            if (attrIds.isEmpty()) {
                return page;
            }
            wrapper.in(Attr::getId, attrIds);
        }

        return attrMapper.selectPage(page, wrapper);
    }

    @Override
    public Attr getDetail(Long attrId) {
        Attr attr = attrMapper.selectById(attrId);
        if (attr == null) {
            throw new BizException(ResultCode.PRODUCT_NOT_FOUND);
        }
        return attr;
    }

    @Override
    public void save(Attr attr) {
        attrMapper.insert(attr);
    }

    @Override
    public void updateById(Attr attr) {
        attrMapper.updateById(attr);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        attrMapper.deleteBatchIds(ids);
    }
}
