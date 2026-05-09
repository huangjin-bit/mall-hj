package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.product.entity.SpuAttrValue;
import com.hj.mall.product.mapper.SpuAttrValueMapper;
import com.hj.mall.product.service.SpuAttrValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpuAttrValueServiceImpl implements SpuAttrValueService {

    private final SpuAttrValueMapper spuAttrValueMapper;

    @Override
    public List<SpuAttrValue> listBySpuId(Long spuId) {
        return spuAttrValueMapper.selectList(
                new LambdaQueryWrapper<SpuAttrValue>()
                        .eq(SpuAttrValue::getSpuId, spuId)
                        .orderByAsc(SpuAttrValue::getSort)
        );
    }

    @Override
    @Transactional
    public void saveBatch(List<SpuAttrValue> values) {
        for (SpuAttrValue value : values) {
            spuAttrValueMapper.insert(value);
        }
    }

    @Override
    @Transactional
    public void removeBySpuId(Long spuId) {
        spuAttrValueMapper.delete(
                new LambdaQueryWrapper<SpuAttrValue>().eq(SpuAttrValue::getSpuId, spuId)
        );
    }
}
