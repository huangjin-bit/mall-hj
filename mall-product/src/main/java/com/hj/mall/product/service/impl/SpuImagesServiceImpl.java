package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.product.entity.SpuImages;
import com.hj.mall.product.mapper.SpuImagesMapper;
import com.hj.mall.product.service.SpuImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpuImagesServiceImpl implements SpuImagesService {

    private final SpuImagesMapper spuImagesMapper;

    @Override
    public List<SpuImages> listBySpuId(Long spuId) {
        return spuImagesMapper.selectList(
                new LambdaQueryWrapper<SpuImages>()
                        .eq(SpuImages::getSpuId, spuId)
                        .orderByAsc(SpuImages::getSort)
        );
    }

    @Override
    @Transactional
    public void saveBatch(Long spuId, List<String> images) {
        for (int i = 0; i < images.size(); i++) {
            SpuImages img = new SpuImages();
            img.setSpuId(spuId);
            img.setImgUrl(images.get(i));
            img.setSort(i);
            spuImagesMapper.insert(img);
        }
    }

    @Override
    @Transactional
    public void removeBySpuId(Long spuId) {
        spuImagesMapper.delete(
                new LambdaQueryWrapper<SpuImages>().eq(SpuImages::getSpuId, spuId)
        );
    }
}
