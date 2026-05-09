package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.product.entity.SkuSaleAttrValue;
import com.hj.mall.product.mapper.SkuSaleAttrValueMapper;
import com.hj.mall.product.service.SkuSaleAttrValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkuSaleAttrValueServiceImpl implements SkuSaleAttrValueService {

    private final SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public List<SkuSaleAttrValue> listBySkuId(Long skuId) {
        return skuSaleAttrValueMapper.selectList(
                new LambdaQueryWrapper<SkuSaleAttrValue>()
                        .eq(SkuSaleAttrValue::getSkuId, skuId)
                        .orderByAsc(SkuSaleAttrValue::getSort)
        );
    }

    @Override
    public List<SkuSaleAttrValue> listBySpuId(Long spuId) {
        return skuSaleAttrValueMapper.selectList(
                new LambdaQueryWrapper<SkuSaleAttrValue>()
                        .eq(SkuSaleAttrValue::getSpuId, spuId)
        );
    }

    @Override
    @Transactional
    public void saveBatch(List<SkuSaleAttrValue> values) {
        for (SkuSaleAttrValue value : values) {
            skuSaleAttrValueMapper.insert(value);
        }
    }

    @Override
    @Transactional
    public void removeBySkuId(Long skuId) {
        skuSaleAttrValueMapper.delete(
                new LambdaQueryWrapper<SkuSaleAttrValue>().eq(SkuSaleAttrValue::getSkuId, skuId)
        );
    }
}
