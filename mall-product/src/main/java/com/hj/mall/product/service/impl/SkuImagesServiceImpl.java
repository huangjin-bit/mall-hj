package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.product.entity.SkuImages;
import com.hj.mall.product.mapper.SkuImagesMapper;
import com.hj.mall.product.service.SkuImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkuImagesServiceImpl implements SkuImagesService {

    private final SkuImagesMapper skuImagesMapper;

    @Override
    public List<SkuImages> listBySkuId(Long skuId) {
        return skuImagesMapper.selectList(
                new LambdaQueryWrapper<SkuImages>()
                        .eq(SkuImages::getSkuId, skuId)
                        .orderByAsc(SkuImages::getSort)
        );
    }

    @Override
    @Transactional
    public void saveBatch(Long skuId, List<SkuImages> images) {
        for (SkuImages img : images) {
            img.setSkuId(skuId);
            skuImagesMapper.insert(img);
        }
    }

    @Override
    @Transactional
    public void removeBySkuId(Long skuId) {
        skuImagesMapper.delete(
                new LambdaQueryWrapper<SkuImages>().eq(SkuImages::getSkuId, skuId)
        );
    }
}
